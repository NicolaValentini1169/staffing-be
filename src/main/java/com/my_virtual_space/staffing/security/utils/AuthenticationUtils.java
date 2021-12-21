package com.my_virtual_space.staffing.security.utils;

import com.my_virtual_space.staffing.security.entities.JWTUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class AuthenticationUtils {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationUtils.class);

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private static JWTUserDetail getLoggedUser() {
        try {
            Authentication auth = getAuthentication();

            if (auth != null && auth.getDetails() instanceof JWTUserDetail) {
                return ((JWTUserDetail) auth.getDetails());
            } else if (auth != null && auth.getPrincipal() instanceof JWTUserDetail) {
                return ((JWTUserDetail) auth.getPrincipal());
            }

        } catch (Exception e) {
            log.error("Error getting logged user", e);
        }

        return null;
    }

    public static UUID getUserId() {
        JWTUserDetail loggedUser = getLoggedUser();

        return loggedUser != null
                ? loggedUser.getId()
                : null;
    }

    public static String getUserStringId() {
        UUID userId = getUserId();

        return userId != null
                ? userId.toString()
                : null;
    }

    public static String getUserCn() {
        JWTUserDetail loggedUser = getLoggedUser();

        return loggedUser != null
                ? loggedUser.getUsername()
                : null;
    }

    public static String getUserAuthority() {
        try {
            return ((SimpleGrantedAuthority) getAuthentication().getAuthorities().toArray()[0]).getAuthority();

        } catch (Exception e) {
            log.error("Error getting logged user", e);
        }

        return null;
    }

}
