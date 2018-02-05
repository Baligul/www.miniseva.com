package com.miniseva.security.account;

import com.miniseva.security.role.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Core Spring DAO to load user data. Used throughout the Spring framework.
 *
 * Service layer used by application to access account information.
 */
@Service("accountDetailsService")
public class AccountDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(AccountDetailsService.class);
    private final AccountRepository repoAccount;

    @Inject
    public AccountDetailsService(AccountRepository repoAccount) {
        this.repoAccount = repoAccount;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repoAccount.findByUsername(username);
        if (null == account) {
            log.info("User not found. Unable to find user with username = " + username);
            throw new UsernameNotFoundException("User not found. Unable to find user with username = " + username);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : account.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        // TODO: IMPLEMENT THE FOLLOWING HARD CODED Account PROPERTIES
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new User(account.getUsername(), account.getPassword(), account.getEnabled(), accountNonExpired,
                credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }
}
