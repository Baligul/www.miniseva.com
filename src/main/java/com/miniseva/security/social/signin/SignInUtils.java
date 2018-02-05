package com.miniseva.security.social.signin;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public class SignInUtils {
    /**
     * Set the current user (i.e. authentication) to the provided userId. The effect of creating an authentication is to
     * programmatically log in a user.
     *
     * @param userDetails current user's user details
     * @param authorities current user's roles
     */
    public static void signin(UserDetails userDetails, Set<GrantedAuthority> authorities) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, authorities));
    }
}
