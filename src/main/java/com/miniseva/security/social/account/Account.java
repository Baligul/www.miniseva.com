package com.miniseva.security.social.account;

/**
 * Account object used by Spring Social's JDBC connection.
 */
public class Account {

    // Properties
    // ==========

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private boolean enabled;

    // Constructors
    // ============

    public Account(String firstName, String lastName, String username, String email, String password, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    // Getters and Setters
    // ===================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // toString
    // ========

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
                + ", username='" + username + '\'' + ", email='" + email + '\'' + ", password='" + password + '\''
                + '}';
        //+ ", createdOn=" + (createdOn != null ? createdOn.toString() : "") + '}';
    }
}
