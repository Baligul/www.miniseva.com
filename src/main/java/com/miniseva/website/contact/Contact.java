package com.miniseva.website.contact;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "web_contact_us")
public class Contact implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Contact.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 256)
    private String fullname;

    @NotNull
    @Size(min = 2, max = 256)
    @Email
    private String email;

    @NotNull
    @Size(min = 2)
    private String note;

    @Column(name = "created_on", columnDefinition = "CURRENT_TIMESTAMP", insertable = true, updatable = false)
    private DateTime createdOn;

    @Transient
    private int createdYear;

    @Transient
    private int createdMonth;

    @Transient
    private int createdDay;

    @Transient
    private String createdStrMonth;

    @Transient
    private String createdOnFormatted;

    protected Contact() {
    }

    public Contact(Long id, String fullname, String email, String note, DateTime createdOn) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.note = note;
        this.createdOn = createdOn;
    }

    @PrePersist
    private void onCreate() {
        setCreatedOn(DateTime.now());
    }

    @PostLoad
    private void onLoad() {
        this.createdYear = createdOn.getYear();
        this.createdMonth = createdOn.getMonthOfYear();
        this.createdDay = createdOn.getDayOfMonth();
        this.createdStrMonth = createdOn.toString("MMM");
        this.createdOnFormatted = this.createdStrMonth + " " + this.createdDay + ", " + this.createdYear;
    }

    // Getters and Setters
    // ===================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public int getCreatedYear() {
        return createdYear;
    }

    public int getCreatedMonth() {
        return createdMonth;
    }

    public int getCreatedDay() {
        return createdDay;
    }

    public String getCreatedStrMonth() {
        return createdStrMonth;
    }

    public String getCreatedOnFormatted() {
        return createdOnFormatted;
    }

    @Override
    public String toString() {
        return "Contact{" +
                ", id=" + (id != null ? id : "") +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", note='" + note + '\'' +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                '}';
    }

}