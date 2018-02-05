package com.miniseva.app.lead;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import org.joda.time.DateTime;

import javax.validation.constraints.*;

import com.miniseva.security.account.Account;
import com.miniseva.app.customer.Customer;

import com.miniseva.app.state.State;
import com.miniseva.app.state.StateConverter;

import java.util.List;

@Entity
public class Lead implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Lead.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(unique = true)
    private String name;

    @NotNull
    @Size(min = 1, max = 256)
    private String address1;

    private String city;

    @Convert(converter = StateConverter.class)
    private State state;

    @Column(name = "postal_code")
    private String postalCode;
    
    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_on")
    private DateTime createdOn;

    @Column(name = "updated_by")
    private Long updatedBy;
    
    @Column(name = "updated_on")
    private DateTime updatedOn;

    protected Lead() {
    }

    public Lead(Long id, String name, String address1, String city, 
                State state, String postalCode,Long createdBy,
                DateTime createdOn, Long updatedBy, DateTime updatedOn) {

        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    @Override
    public String toString() {
        return "Lead{" +
                "id=" + (id != null ? id : "") +
                ", name='" + name + '\'' +
                ", address1='" + address1 + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", state='" + state.toString() + '\'' +
                ", createdBy=" + createdBy +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                ", updatedBy=" + updatedBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                '}';
    }

}