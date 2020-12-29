package com.challenge.gateway.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    ROLE_API_CONSUMER("role:api:consumer", Sets.newHashSet(ApplicationUserPermission.PERMISSION_ITINERARIES_READ, ApplicationUserPermission.PERMISSION_SEARCH_READ)),
    ROLE_API_ADMIN("role:api:admin", Sets.newHashSet(ApplicationUserPermission.PERMISSION_ITINERARIES_READ, ApplicationUserPermission.PERMISSION_ITINERARIES_WRITE, ApplicationUserPermission.PERMISSION_SEARCH_READ));

    private String name;
    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(String name, Set<ApplicationUserPermission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.getName()));
        return permissions;
    }

}
