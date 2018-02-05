package com.miniseva.app.item;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import org.springframework.format.annotation.DateTimeFormat;;

import javax.validation.constraints.*;

import java.util.List;

@Entity
public class Item implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Item.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=1, max=256)
    private String name;

    private int mrp;

    @Min(1)
    private int price;
    
    private String description;

    @Column(name = "img_path")
    private String imgPath;

    @NotNull
    @Column(name = "product_id")
    private Long productId;

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

    @Transient
    private String productName;
    
    protected Item() {
    }

    public Item(Long id, String name, int mrp, int price, String description, String imgPath, Long productId,
            Long updatedBy, Long createdBy, DateTime updatedOn, DateTime createdOn) {

        this.id = id;
        this.name = name;
        this.mrp = mrp;
        this.price = price;
        this.description = description;
        this.imgPath = imgPath;
        this.productId = productId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Item{" +
                ", id=" + (id != null ? id : "") +
                ", name=" + (name != null ? name : "") +
                ", price=" + price +
                ", mrp=" + mrp +
                ", description=" + (description != null ? description : "") +
                ", imgPath=" + (imgPath != null ? imgPath : "") +
                ", productId=" + (productId != null ? productId : "") +
                ", updatedBy=" + updatedBy +
                ", createdBy=" + createdBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                '}';
    }

}