package com.miniseva.common.newsletter_signup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;

@Entity
public class NewsletterSignup implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(NewsletterSignup.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    @Email
    private String email;

    protected NewsletterSignup() {
    }

    public NewsletterSignup(String email) {
        this.email = email;
    }

    // Getters and Setters
    // ===================

    public static Logger getLog() {
        return log;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NewsletterSignup{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}