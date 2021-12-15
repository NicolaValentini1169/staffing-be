package com.my_virtual_space.staffing.security.constants;

import java.util.UUID;

public class SecurityConstants {

    /*-- JWT Constants --*/
    public static final String SECRET = UUID.randomUUID().toString();
    public static final long EXIPIRATION_TIME = 43_200_000; //12 ore
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    /*-- Roles Constants --*/
    public static final String ROLE_USER_VALUE = "ROLE_USER";
    public static final String ROLE_MODERATOR_VALUE = "ROLE_MODERATOR";
    public static final String ROLE_ADMIN_VALUE = "ROLE_ADMIN";

}
