package com.miniseva.app.schedule;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import org.springframework.format.annotation.DateTimeFormat;;

import javax.validation.constraints.*;

import java.util.List;

@Entity
public class Schedule implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Schedule.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_discount")
    private int itemDiscount;

    @Column(name = "item_price")
    private int itemPrice;
    
    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_product_id")
    private Long itemProductId;

    private String slot;

    @Column(name = "is_scheduled")
    private int isScheduled;

    private String dates;
    
    private int quantity;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "order_id")
    private Long orderId;

    private String status;

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

    public Schedule() {
    }

    public Schedule(Long id, Long itemId, String itemName, int itemDiscount, int itemPrice, 
                    String itemDescription, Long itemProductId, String slot, int isScheduled,
                    String dates, int quantity, int totalPrice, long orderId, String status,
                    Long updatedBy, Long createdBy, DateTime updatedOn, DateTime createdOn) {

        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDiscount = itemDiscount;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemProductId = itemProductId;
        this.slot = slot;
        this.isScheduled = isScheduled;
        this.dates = dates;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
        this.status = status;
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
    }

    // Getters and Setters
    // ===================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(int itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Long getItemProductId() {
        return itemProductId;
    }

    public void setItemProductId(Long itemProductId) {
        this.itemProductId = itemProductId;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public int getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(int isScheduled) {
        this.isScheduled = isScheduled;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "Schedule{" +
                "id=" + (id != null ? id : "") +
                ", itemId=" + (itemId != null ? itemId : "") +
                ", itemName=" + (itemName != null ? itemName : "") +
                ", itemPrice=" + itemPrice +
                ", itemDiscount=" + itemDiscount +
                ", itemDescription=" + (itemDescription != null ? itemDescription : "") +
                ", itemProductId=" + (itemProductId != null ? itemProductId : "") +
                ", slot=" + (slot != null ? slot : "") +
                ", isScheduled=" + isScheduled +
                ", dates=" + (dates != null ? dates : "") +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", orderId=" + (orderId != null ? orderId : "") +
                ", status=" + (status != null ? status : "") +
                ", updatedBy=" + updatedBy +
                ", createdBy=" + createdBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                '}';
    }

}