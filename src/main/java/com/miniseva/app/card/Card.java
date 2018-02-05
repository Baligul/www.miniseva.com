package com.miniseva.app.card;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import org.springframework.format.annotation.DateTimeFormat;;

import javax.validation.constraints.*;

import java.util.List;

@Entity
public class Card implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Card.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=12, max=12)
    @Column(name = "card_no")
    private String cardNo;

    @Min(1)
    @Max(2500)
    private int amount;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "expires_on")
    private DateTime expiresOn;

    @Column(name = "updated_by")
    private Long updatedBy;

    @NotNull
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_on")
    private DateTime updatedOn;

    @Column(name = "created_on")
    private DateTime createdOn;

    @Transient
    private int expiresOnYear;

    @Transient
    private int expiresOnMonth;

    @Transient
    private int expiresOnDay;

    @Transient
    private String expiresOnStrMonth;
    
    @Transient
    private String expiresOnFormatted;

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

    @Transient
    private String formattedExpiresOnStrMonth;

    @Transient
    private String formattedExpiresOnStrDay;

    @Transient
    private String formattedExpiresOn;

    protected Card() {
    }

    public Card(Long id, String cardNo, int amount, DateTime expiresOn,
            Long updatedBy, Long createdBy, DateTime updatedOn, DateTime createdOn) {

        this.id = id;
        this.cardNo = cardNo;
        this.amount = amount;
        this.expiresOn = expiresOn;
        this.updatedBy = updatedBy;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
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

        this.expiresOnYear = expiresOn.getYear();
        this.expiresOnMonth = expiresOn.getMonthOfYear();
        this.expiresOnDay = expiresOn.getDayOfMonth();
        this.expiresOnStrMonth = expiresOn.toString("MMM");
        this.formattedExpiresOnStrMonth = expiresOn.toString("MM");
        this.formattedExpiresOnStrDay = expiresOn.toString("dd");

        this.expiresOnFormatted = this.expiresOnStrMonth + " " + this.expiresOnDay + ", " + this.expiresOnYear;
        this.formattedExpiresOn = this.expiresOnYear + "-" + formattedExpiresOnStrMonth + "-" + formattedExpiresOnStrDay;
    }

    // Getters and Setters
    // ===================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public DateTime getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(DateTime expiresOn) {
        this.expiresOn = expiresOn;
    }

    public int getExpiresOnYear() {
        return expiresOnYear;
    }

    public int getExpiresOnMonth() {
        return expiresOnMonth;
    }

    public int getExpiresOnDay() {
        return expiresOnDay;
    }

    public String getExpiresOnStrMonth() {
        return expiresOnStrMonth;
    }

    public String getExpiresOnFormatted() {
        return expiresOnFormatted;
    }

    public String getFormattedExpiresOnStrMonth() {
        return formattedExpiresOnStrMonth;
    }

    public String getFormattedExpiresOnStrDay() {
        return formattedExpiresOnStrDay;
    }

    public void setFormattedExpiresOn() {
        if(this.expiresOn == null) {
            this.expiresOn = DateTime.now().plusMonths(1);
        }
        this.expiresOnYear = expiresOn.getYear();
        this.formattedExpiresOnStrMonth = expiresOn.toString("MM");
        this.formattedExpiresOnStrDay = expiresOn.toString("dd");

        this.formattedExpiresOn = this.expiresOnYear + "-" + formattedExpiresOnStrMonth + "-" + formattedExpiresOnStrDay;
    }

    public String getFormattedExpiresOn() {
        return formattedExpiresOn;
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
        return "Card{" +
                ", id=" + (id != null ? id : "") +
                ", cardNo=" + (cardNo != null ? cardNo : "") +
                ", amount='" + amount + '\'' +
                ", expiresOn=" + (expiresOn != null ? expiresOn.toString() : "") +
                ", updatedBy=" + updatedBy +
                ", createdBy=" + createdBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                '}';
    }

}