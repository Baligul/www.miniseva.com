package com.miniseva.app.accountrolemap;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import javax.validation.constraints.*;

@Entity
public class AccountRoleMap implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(AccountRoleMap.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "created_on")
    private DateTime createdOn;

    protected AccountRoleMap() {
    }

    public AccountRoleMap(Long id, Long accountId, Long roleId, DateTime createdOn) {

        this.id = id;
        this.accountId = accountId;
        this.roleId = roleId;
        this.createdOn = createdOn;

    }

    // Getters and Setters
    // ===================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "AccountRoleMap{" +
                ", id=" + (id != null ? id : "") +
                ", accountId=" + (accountId != null ? accountId : "") +
                ", roleId=" + (roleId != null ? roleId : "") +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                '}';
    }

}