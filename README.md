# Currency Converter - JavaFX Application

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
- [Running the Project](#running-the-project)
- [User Authentication](#user-authentication)
- [Converter](#converter)
- [Dashboard](#dashboard)
- [History Chart](#history-chart)
- [Dark Mode](#dark-mode)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Introduction
This **Currency Converter** is a JavaFX application that allows users to convert currencies between different exchange rates using real-time data from an API. It features user authentication (sign up/log in), a currency conversion tool, a dashboard displaying user statistics, and a historical chart to visualize past conversions.

## Features
- User authentication (sign up and log in)
- Currency conversion with real-time exchange rates
- Dynamic dashboard displaying total conversions and preferred currency
- Conversion history visualization via line chart
- Toggleable dark mode for UI customization
- API integration to fetch exchange rates

## Getting Started

### Prerequisites
Before running the project, make sure you have the following installed:
- [Java Development Kit (JDK) 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
- [NetBeans 20](https://netbeans.apache.org/download/)
- [Git](https://git-scm.com/)
- Internet connection (for fetching live exchange rates)

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/JeronOkkers/currency-converter.git
    cd currency-converter
    ```

2. Open the project in NetBeans or your preferred IDE.

3. Make sure your project is set up with JavaFX and other necessary libraries (refer to the `pom.xml` or build file if needed).

## Running the Project
1. Launch the application from your IDE by running `CurrencyConvertorTest.java`.
2. Log in or sign up as a new user to access the converter.
3. Convert currencies, view your history, and use the dashboard to track your statistics.

## User Authentication
- When you first start the application, you’ll be prompted to log in or sign up.
- Sign up requires you to provide a username, password, preferred currency, and an option to enable dark mode.
- Log in to access your user data and continue using the converter.

## Converter
The currency converter allows you to:
- Enter an amount.
- Choose the source currency and target currency from dropdowns.
- View the converted result in real-time.

## Dashboard
The dashboard provides statistics for:
- Total conversions made by the user.
- The user's preferred currency.

## History Chart
A line chart shows the history of your conversions, plotting the amounts against the order in which the conversions were made.

## Dark Mode
You can toggle between light and dark modes using the "Toggle Dark Mode" button. Your preference is saved and applied every time you log in.

## Technologies Used
- **JavaFX** for the user interface.
- **Google Gson** for JSON parsing.
- **HTTP API** for fetching live exchange rates.
- **Swing** for user authentication prompts.
- **JUnit** (optional) for testing.

## Contributing
Contributions are welcome! Please follow these steps to contribute:
1. Fork the repository.
2. Create a new branch for your feature or bugfix:
    ```bash
    git checkout -b feature-name
    ```
3. Make your changes and commit them:
    ```bash
    git commit -m "Add new feature"
    ```
4. Push your branch to GitHub:
    ```bash
    git push origin feature-name
    ```
5. Open a pull request with a detailed description of your changes.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
