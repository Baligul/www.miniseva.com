package com.miniseva.security.role;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public interface RoleService {
    /**
     * Get a specific role by name.
     *
     * @param name Name of the role to get.
     * @return A set containing a single role.
     */
    Set<Role> findByRole(String name);

    /**
     * Convert roles to granted authorities.
     *
     * @param roles A set of Role.
     * @return A set of GrantedAuthority.
     */
    Set<GrantedAuthority> rolesToGrantedAuthorities(Set<Role> roles);
}
