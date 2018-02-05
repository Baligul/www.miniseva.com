package com.miniseva.security.services;

import com.miniseva.security.account.AccountDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementation of SecurityService for Username/Password authentication.
 */
@Service
public class SecurityServiceUsernamePassword implements SecurityService {
    private static final Logger log = LoggerFactory.getLogger(SecurityServiceUsernamePassword.class);
    private AuthenticationManager authenticationManager;
    private AccountDetailsService accountDetailsService;

    @Autowired
    public SecurityServiceUsernamePassword(AuthenticationManager authenticationManager,
            AccountDetailsService accountDetailsService) {
        this.authenticationManager = authenticationManager;
        this.accountDetailsService = accountDetailsService;
    }

    @Override
    public String findLoggedInUsername() {
        Object accountDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (accountDetails instanceof UserDetails) {
            return ((UserDetails) accountDetails).getUsername();
        }

        return null;
    }

    @Override
    public void autologin(String username, String password) {
        UserDetails accountDetails = accountDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                accountDetails, password, accountDetails.getAuthorities());

        authenticationManager.authenticate(authToken);

        if (authToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.debug(String.format("Auto log in successful. username = %s", username));
        }
    }
}
