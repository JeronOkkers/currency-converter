/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package currencyconvertortest;

import currencyconvertortest.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author okker
 */
public class User {
    private String username;
    private String password;
    private List<String> savedPairs;
    private List<String> conversionHistory;
    private String preferredCurrency;
    private boolean darkMode; // Example setting
    
    public User(String username, String password, String preferredCurrency, boolean darkMode) {
        this.username = username;
        this.password = password;
        this.savedPairs = new ArrayList<>();
        this.conversionHistory = new ArrayList<>();
        this.preferredCurrency = preferredCurrency; // Default value
        this.darkMode = darkMode; // Default value
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    public void addSavedPair(String pair) {
        savedPairs.add(pair);
    }
    
    public void addConversionHistory(String history) {
        conversionHistory.add(history);
        UserManager manage = new UserManager();
        manage.saveUsersToFile();
    }
    
    // Getters for savedPairs and conversionHistory
    public List<String> getSavedPairs() {
        return savedPairs;
    }

    public List<String> getConversionHistory() {
        return conversionHistory;
    }

 public String getPreferredCurrency() {
        return preferredCurrency;
    }

    public void setPreferredCurrency(String preferredCurrency) {
        this.preferredCurrency = preferredCurrency;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }
    
}
