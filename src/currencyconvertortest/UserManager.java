    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package currencyconvertortest;
    import currencyconvertortest.User;

    import java.io.*;
    import java.util.HashMap;
    import java.util.Map;

    public class UserManager {
        private static final String FILE_PATH = "users.txt";
        private HashMap<String, User> users;

        public UserManager() {
            users = new HashMap<>();
            loadUsersFromFile();
        }

        // Load users from file
        private void loadUsersFromFile() {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 4) { // Ensure it matches the expected data fields
                        String username = parts[0];
                        String email = parts[1];
                        String preferredCurrency = parts[2];
                        boolean darkMode = Boolean.parseBoolean(parts[3]);

                        // Create a User object with the extracted data
                        User user = new User(username, email, preferredCurrency, darkMode);

                        // Add the User object to the HashMap
                        users.put(username, user);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading users from file: " + e.getMessage());
            }
        }

        // Save users to file
        public void saveUsersToFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (User user : users.values()) {
                    writer.write(user.getUsername() + ":" + user.getPassword() + ":" +
                                 user.getPreferredCurrency() + ":" + user.isDarkMode());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving users to file: " + e.getMessage());
            }
        }

        // Log in
        public User logIn(String username, String password) {
            User user = users.get(username);
            if (user != null && user.validatePassword(password)) {
                // Update preferred currency and dark mode from stored values
                user.setPreferredCurrency(users.get(username).getPreferredCurrency());
                user.setDarkMode(users.get(username).isDarkMode());

                // Apply dark mode to UI elements (if applicable)
                if (user.isDarkMode()) {
                    // Apply your dark mode style sheet or CSS rules here
                } else {
                    // Apply your default style sheet or CSS rules here
                }

                return user;
            }
            return null;
        }

        // Sign up
        public boolean signUp(String username, String password, String prefferedCurrency, boolean darkMode) {
            if (!users.containsKey(username)) {
                User newUser = new User(username, password, prefferedCurrency, darkMode);
                users.put(username, newUser);
                saveUsersToFile();
                return true;
            }
            return false;
        }
    }
