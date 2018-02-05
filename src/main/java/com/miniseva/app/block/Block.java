package com.miniseva.app.block;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import javax.validation.constraints.*;

import com.miniseva.security.account.Account;

import java.util.List;

@Entity
public class Block implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Block.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 256)
    private String name;

    @NotNull
    @Column(name = "lead_id")
    private Long leadId;

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

    protected Block() {
    }

    public Block(Long id, String name, Long leadId,Long createdBy, 
                   DateTime createdOn, Long updatedBy, DateTime updatedOn) {

        this.id = id;
        this.name = name;
        this.leadId = leadId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLeadId() {
        return leadId;
    }

    public void setLeadId(Long leadId) {
        this.leadId = leadId;
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

    @Override
    public String toString() {
        return "Block{" +
                ", id=" + (id != null ? id : "") +
                ", name='" + name + '\'' +
                ", leadId=" + (leadId != null ? leadId : "") +
                ", createdBy=" + createdBy +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                ", updatedBy=" + updatedBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                '}';
    }

}