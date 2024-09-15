package currencyconvertortest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyConvertorTest extends Application {

    private TextField amountField;
    private ComboBox<String> fromCurrencyBox;
    private ComboBox<String> toCurrencyBox;
    private Label resultLabel;
    private static User currentUser;
    private UserManager userManager;
    private LineChart<Number, Number> historyChart;
    private BorderPane dashboardPane;  // Store dashboardPane to update dynamically

    final String API_ENDPOINT = "https://v6.exchangerate-api.com/v6/9a4f2127b9c160b57e8f68a4/latest/USD";
    static String[] CURRENCIES = {
            "USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG",
            "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB",
            "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP",
            "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD",
            "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP",
            "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG",
            "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD",
            "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD",
            "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA",
            "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR",
            "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN",
            "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF",
            "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD",
            "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY",
            "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VEF", "VND",
            "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW",
            "ZWL"
    };

    @Override
    public void start(Stage primaryStage) {
        userManager = new UserManager();
        showLogin(); // Handle login before launching the app
    }

    private void showLogin() {
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showOptionDialog(null, "Would you like to sign up or log in?",
                    "User Authentication", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Log in", "Sign up"}, "Log in");

            if (option == JOptionPane.YES_OPTION) {
                String username = JOptionPane.showInputDialog("Enter Username");
                String password = JOptionPane.showInputDialog("Enter Password");
                currentUser = userManager.logIn(username, password);
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                    System.exit(0);
                }
            } else {
                String username = JOptionPane.showInputDialog("Enter Username for Sign Up");
                String password = JOptionPane.showInputDialog("Enter Password for Sign Up");
                String preferredCurrency = JOptionPane.showInputDialog("Enter Preferred Currency");
                boolean darkMode = JOptionPane.showConfirmDialog(null, "Dark Mode?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

                if (userManager.signUp(username, password, preferredCurrency, darkMode)) {
                    JOptionPane.showMessageDialog(null, "Sign up successful! You can now log in.");
                    currentUser = userManager.logIn(username, password);
                } else {
                    JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different one.");
                    System.exit(0);
                }
            }

            if (currentUser != null) {
                Platform.runLater(() -> createMainStage());
            }
        });
    }

    private void createMainStage() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Currency Converter - JavaFX");

        TabPane tabPane = new TabPane();
        Tab converterTab = new Tab("Converter");
        Tab historyTab = new Tab("History Stats");
        Tab dashboardTab = new Tab("Dashboard");

        // Converter Tab
        GridPane converterPane = createConverterPane();
        converterTab.setContent(converterPane);

        // History Tab
        historyChart = createHistoryChart();
        historyTab.setContent(historyChart);

        // Dashboard Tab
        dashboardPane = createDashboardPane();
        dashboardTab.setContent(dashboardPane);

        tabPane.getTabs().addAll(converterTab, historyTab, dashboardTab);

        // Create the scene BEFORE applying the stylesheet
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);

        // Apply initial CSS stylesheet based on current dark mode setting
        applyStylesheet(primaryStage, currentUser.isDarkMode());

        primaryStage.show();
    }

    private GridPane createConverterPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label amountLabel = new Label("Amount:");
        amountField = new TextField("1");

        Label fromLabel = new Label("From:");
        fromCurrencyBox = new ComboBox<>();
        fromCurrencyBox.getItems().addAll(CURRENCIES);
        fromCurrencyBox.setValue(currentUser.getPreferredCurrency());

        Label toLabel = new Label("To:");
        toCurrencyBox = new ComboBox<>();
        toCurrencyBox.getItems().addAll(CURRENCIES);
        toCurrencyBox.setValue("EUR");

        Button convertButton = new Button("Convert");
        convertButton.setOnAction(e -> handleConvert());

        resultLabel = new Label();

        Button toggleDarkModeButton = new Button("Toggle Dark Mode");
        toggleDarkModeButton.setOnAction(e -> toggleDarkMode());

        gridPane.add(amountLabel, 0, 0);
        gridPane.add(amountField, 1, 0);
        gridPane.add(fromLabel, 0, 1);
        gridPane.add(fromCurrencyBox, 1, 1);
        gridPane.add(toLabel, 0, 2);
        gridPane.add(toCurrencyBox, 1, 2);
        gridPane.add(convertButton, 1, 3);
        gridPane.add(resultLabel, 1, 4);
        gridPane.add(toggleDarkModeButton, 1, 6);

        return gridPane;
    }

    private BorderPane createDashboardPane() {
        BorderPane dashboardPane = new BorderPane();
        updateDashboardPane(dashboardPane);  // Initial update
        return dashboardPane;
    }

    private void updateDashboardPane(BorderPane dashboardPane) {
        Label statsLabel = new Label("User Statistics:\n\n" +
                "Total Conversions: " + currentUser.getConversionHistory().size() + "\n" +
                "Preferred Currency: " + currentUser.getPreferredCurrency());

        dashboardPane.setCenter(statsLabel);
    }

    private void toggleDarkMode() {
        boolean isDarkMode = !currentUser.isDarkMode();
        currentUser.setDarkMode(isDarkMode);
        userManager.saveUsersToFile();
        applyStylesheet((Stage) Stage.getWindows().get(0), currentUser.isDarkMode());
    }

    private void applyStylesheet(Stage stage, boolean darkMode) {
        String stylesheet = darkMode ? "darkmode.css" : "lightmode.css";
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(getClass().getResource(stylesheet).toExternalForm());
    }

    private void updateHistoryChart() {
        if (historyChart != null) {
            historyChart.getData().clear();
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Conversion History");

            List<String> conversionHistory = currentUser.getConversionHistory();

            for (int i = 0; i < conversionHistory.size(); i++) {
                String[] splitData = conversionHistory.get(i).split(":");
                double amount = Double.parseDouble(splitData[1]);
                series.getData().add(new XYChart.Data<>(i + 1, amount));
            }

            historyChart.getData().add(series);
        }
    }

    private LineChart<Number, Number> createHistoryChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Conversion Number");
        yAxis.setLabel("Amount Converted");

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Conversion History");

        updateHistoryChart();
        return chart;
    }

    private void handleConvert() {
        String amountText = amountField.getText();
        try {
            double amount = Double.parseDouble(amountText);
            String fromCurrency = fromCurrencyBox.getValue();
            String toCurrency = toCurrencyBox.getValue();

            double exchangeRate = fetchExchangeRate(fromCurrency, toCurrency);
            double result = amount * exchangeRate;

            resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, fromCurrency, result, toCurrency));

            currentUser.addConversionHistory(fromCurrency + ":" + result);
            userManager.saveUsersToFile();

            // Update dashboard and history after conversion
            updateDashboardPane(dashboardPane);
            updateHistoryChart();
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid amount.");
        } catch (IOException e) {
            Logger.getLogger(CurrencyConvertorTest.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private double fetchExchangeRate(String fromCurrency, String toCurrency) throws IOException {
        URL url = new URL(API_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

            double fromRate = conversionRates.get(fromCurrency).getAsDouble();
            double toRate = conversionRates.get(toCurrency).getAsDouble();

            return toRate / fromRate;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
