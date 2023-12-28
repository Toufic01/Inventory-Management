package com.example.inventorymangement;

public class User {
    public String username;
    public String email;
    public String password;
    public String userType; // New field for user type

    // Updated constructor to match the provided parameters
    public User(String username, String email, String password, String userType) {
        this.username = username;
        this.email = email;
        this.password = password; // Store the password
        this.userType = userType;
    }

    // Getter and setter methods for the userType field
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    // Getter and setter methods for the password field
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }
}
