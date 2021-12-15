package com.my_virtual_space.staffing.dtos.response;

import com.my_virtual_space.staffing.security.entities.Role;

import java.util.UUID;

public class LoginResponse {

    private UUID id;
    private String username;
    private String role;
    private String accessToken;

    public LoginResponse() {
    }

    public LoginResponse(UUID id, String username, String role, String accessToken) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.accessToken = accessToken;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
