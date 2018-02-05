package com.miniseva.security.role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceProvider implements RoleService {
    private RoleRepository roleRepository;

    @Inject
    public RoleServiceProvider(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Role> findByRole(String roleName) {
        Iterable<Role> rolesIterable = roleRepository.findAll();
        List<Role> rolesList = new ArrayList<>();
        rolesIterable.forEach(role -> {
            if (role.getRole().equals(roleName))
                rolesList.add(role);
        });
        return new HashSet<>(rolesList);
    }

    @Override
    public Set<GrantedAuthority> rolesToGrantedAuthorities(Set<Role> roles) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return grantedAuthorities;
    }
}
