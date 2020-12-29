package com.challenge.gateway.security;

public enum ApplicationUserPermission {

    PERMISSION_ITINERARIES_READ("permission:itineraries:read"),
    PERMISSION_ITINERARIES_WRITE("permission:itineraries:write"),
    PERMISSION_SEARCH_READ("permission:search:read");

    private final String name;

    ApplicationUserPermission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
