package com.miniseva.security.custom;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * MiniSeva specific account details.
 */
public class AccountDetails {
    private Authentication authentication = null;
    private boolean loggedIn = false;
    private boolean enabled = false;
    private String username = "";
    private boolean isRoleRoot = false;
    private boolean isRoleCustomer = false;
    private boolean isRoleDeliveryBoy = false;

    // Constructors
    // ============

    public AccountDetails(Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            if (auth instanceof UsernamePasswordAuthenticationToken || auth instanceof RememberMeAuthenticationToken) {
                if (auth.getPrincipal() instanceof UserDetails) {
                    this.loggedIn = true;
                    User user = (User) auth.getPrincipal();
                    if (user != null) {
                        this.username = user.getUsername();
                        this.enabled = user.isEnabled();
                        Collection<GrantedAuthority> authorities = user.getAuthorities();
                        for (GrantedAuthority authority : authorities) {
                            String role = authority.getAuthority();
                            // ROLE_USER and ROLE_ADMIN are Delixus.com roles
                            //if (role != null && role.equals("ROLE_USER"))
                            //    isUserRole = true;
                            //if (role != null && role.equals("ROLE_ADMIN"))
                            //    isAdminRole = true;
                            if (role != null && role.equals("ROLE_ROOT"))
                                this.isRoleRoot = true;
                            if (role != null && role.equals("ROLE_CUSTOMER"))
                                this.isRoleCustomer = true;
                            if (role != null && role.equals("ROLE_DELIVERY_BOY"))
                                this.isRoleDeliveryBoy = true;
                        }
                    }
                } else if (auth.getPrincipal() instanceof String) {
                    loggedIn = true;
                    username = (String) auth.getPrincipal();
                    enabled = true;
                }
            }

            // Social Authentication
            // ---------------------
            // There's no need to check for `auth instanceof AnonymousAuthenticationToken` because the default
            // values satisfy this auth type.
        }
    }

    // Getters and Setters
    // ===================

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isRoot() {
        return isRoleRoot;
    }

    public boolean isCustomer() {
        return isRoleCustomer;
    }

    public boolean isDeliveryBoy() {
        return isRoleDeliveryBoy;
    }
}
