package com.SAN.IGR.utils.security;

public class Role {

    // This role implicitly allows access to all views.
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private Role() {
        // Static methods and fields only
    }

    public static String[] getAllRoles() {
        return new String[] { USER, ADMIN };
    }
}
