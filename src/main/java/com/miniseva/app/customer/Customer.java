package com.miniseva.app.customer;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import javax.validation.constraints.*;

import com.miniseva.security.account.Account;

import java.util.List;

@Entity
public class Customer implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Customer.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    private String no;

    @NotNull
    @Size(min = 12, max = 12)
    @Column(name = "card_no", unique = true)
    private String cardNo;

    @NotNull
    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "block_id")
    private Long blockId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_on")
    private DateTime createdOn;

    @Column(name = "updated_by")
    private Long updatedBy;
    
    @Column(name = "updated_on")
    private DateTime updatedOn;

    @Transient
    private String leadName;

    @Transient
    private String blockName;

    protected Customer() {
    }

    public Customer(Long id, String no, String cardNo, Long leadId, Long blockId,
                    Long createdBy, DateTime createdOn, Long updatedBy, DateTime updatedOn) {

        this.id = id;
        this.no = no;
        this.cardNo = cardNo;
        this.leadId = leadId;
        this.blockId = blockId;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    // Getters and Setters
    // ===================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
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

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                ", id=" + (id != null ? id : "") +
                ", no='" + no + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", leadId=" + (leadId != null ? leadId : "") +
                ", blockId=" + (blockId != null ? blockId : "") +
                ", createdBy=" + createdBy +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                ", updatedBy=" + updatedBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                '}';
    }

}