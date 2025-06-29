package com.zybooks.inventoryapp.model;

public class User {

    // USERNAME FIELD
    private String username;

    // EMAIL FIELD
    private String email;

    // PASSWORD FIELD
    private String password;

    // CONSTRUCTOR FOR REGISTRATION (USERNAME, EMAIL, PASSWORD)
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // CONSTRUCTOR FOR LOGIN (EMAIL, PASSWORD)
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // GETTERS
    public String getUsername() { 
        return username; 
        }
    public String getEmail() { 
        return email; 
        }
    public String getPassword() {
        return password; 
        }

    // SETTERS
    public void setUsername(String username) { 
        this.username = username; 
        }
    public void setEmail(String email) {
         this.email = email; 
         }
    public void setPassword(String password) {
         this.password = password; 
         }
}
