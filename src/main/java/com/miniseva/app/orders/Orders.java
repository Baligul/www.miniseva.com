package com.miniseva.app.orders;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import org.springframework.format.annotation.DateTimeFormat;;

import javax.validation.constraints.*;

import java.util.List;

@Entity
public class Orders implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Orders.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_on")
    private DateTime updatedOn;

    @Column(name = "created_on")
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

    protected Orders() {
    }

    public Orders(Long id, int amount, String currencyCode,
                  Long updatedBy, Long createdBy, DateTime updatedOn, DateTime createdOn) {

        this.id = id;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.updatedBy = updatedBy;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.createdOn = createdOn;

    }

    public Orders(int amount, String currencyCode, DateTime createdOn) {
        this.amount = amount;
        this.currencyCode = currencyCode;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public DateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(DateTime updatedOn) {
        this.updatedOn = updatedOn;
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
        return "Orders{" +
                "id=" + (id != null ? id : "") +
                ", amount=" + amount +
                ", currencyCode=" + (currencyCode != null ? currencyCode : "") +
                ", updatedBy=" + updatedBy +
                ", createdBy=" + createdBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                '}';
    }
}