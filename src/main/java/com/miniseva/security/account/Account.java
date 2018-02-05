package com.miniseva.security.account;

import com.miniseva.security.role.Role;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.*;
import java.util.List;

import org.hibernate.validator.constraints.Email;

import com.miniseva.app.accountrolemap.AccountRoleMap;



@Entity
@Table(name = "account")
public class Account implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Account.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "customer_id", unique = true)
    private Long customerId;

    private String name;
    
    private String username;

    private String email;

    @NotNull
    @Pattern(regexp="(^$|[0-9]{10})")
    @Column(name = "mobile_phone")
    private String mobilePhone;

    @NotNull
    @Size(min = 2, max = 256)
    private String password;

    private boolean enabled;
    private boolean expired;
    private boolean locked;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired;

    @Column(name = "last_login")
    private DateTime lastLogin;

    @Column(name = "created_on", columnDefinition = "CURRENT_TIMESTAMP", updatable = false)
    private DateTime createdOn;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_on")
    private DateTime updatedOn;

    @Column(name = "updated_by")
    private Long updatedBy;

    private boolean approved;

    private boolean x;

    @Transient
    private int lastLoginYear;

    @Transient
    private int lastLoginDay;

    @Transient
    private String lastLoginMonth;

    @Transient
    private String lastLoginTime;

    @Transient
    private String lastLoginFormatted;

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
    private String cardNo;
    
    protected Account() {
    }

    public Account(Long id, String name, String username, String email, 
            String mobilePhone, String password, boolean enabled, 
            boolean expired, boolean locked, boolean credentialsExpired,
            DateTime lastLogin, DateTime createdOn, Long createdBy, 
            DateTime updatedOn, Long updatedBy, boolean approved, boolean x) {

        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.password = password;
        this.enabled = enabled;
        this.expired = expired;
        this.locked = locked;
        this.credentialsExpired = credentialsExpired;
        this.lastLogin = lastLogin;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.updatedOn = updatedOn;
        this.updatedBy = updatedBy;
        this.approved = approved;
        this.x = x;
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

        if (lastLogin != null) {
            this.lastLoginYear = lastLogin.getYear();
            this.lastLoginDay = lastLogin.getDayOfMonth();
            this.lastLoginMonth = lastLogin.toString("MMM");
            this.lastLoginTime = lastLogin.toString("hh:mm a");
            this.lastLoginFormatted = this.lastLoginMonth + " " + this.lastLoginDay + ", " + this.lastLoginYear + " at " + lastLoginTime;
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public DateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(DateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(DateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
       this.approved = approved;
    }
    
    public boolean getX() {
        return x;
    }

    public void setX(boolean x) {
       this.x = x;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public int getLastLoginYear() {
        return lastLoginYear;
    }

    public void setLastLoginYear(int lastLoginYear) {
        this.lastLoginYear = lastLoginYear;
    }

    public int getLastLoginDay() {
        return lastLoginDay;
    }

    public void setLastLoginDay(int lastLoginDay) {
        this.lastLoginDay = lastLoginDay;
    }

    public String getLastLoginMonth() {
        return lastLoginMonth;
    }

    public void setLastLoginMonth(String lastLoginMonth) {
        this.lastLoginMonth = lastLoginMonth;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginFormatted() {
        return lastLoginFormatted;
    }

    public void setLastLoginFormatted(String lastLoginFormatted) {
        this.lastLoginFormatted = lastLoginFormatted;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "account_role_map", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String toString() {
        return "Account{" +
                ", id=" + (id != null ? id : "") +
                ", customerId=" + customerId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", expired=" + expired +
                ", locked=" + locked +
                ", credentialsExpired=" + credentialsExpired +
                ", lastLogin=" + (lastLogin != null ? lastLogin.toString() : "") +
                ", createdOn=" + (createdOn != null ? createdOn.toString() : "") +
                ", createdBy=" + createdBy +
                ", updatedOn=" + (updatedOn != null ? updatedOn.toString() : "") +
                ", updatedBy=" + updatedBy +
                ", approved=" + approved +
                ", x=" + x +
                '}';
    }

}
