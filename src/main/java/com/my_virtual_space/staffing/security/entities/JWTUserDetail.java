package com.my_virtual_space.staffing.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.my_virtual_space.staffing.security.utils.SimpleGrantedAuthorityDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

public class JWTUserDetail implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    private Long iat;
    private Long exp;
    /**
     * User username
     */
    private String sub;
    /**
     * User roles
     */
    @JsonDeserialize(using = SimpleGrantedAuthorityDeserializer.class)
    private Collection<? extends GrantedAuthority> roles;
    /**
     * User id
     */
    private UUID id;
    /**
     * User username
     */
    private String cn;
    /**
     * User password
     */
    private String password;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return cn;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder {
        private final JWTUserDetail jwtUserDetail;

        public Builder() {
            this.jwtUserDetail = new JWTUserDetail();
        }

        public Builder setSub(String sub) {
            this.jwtUserDetail.sub = sub;
            return this;
        }

        public Builder setIat(Long iat) {
            this.jwtUserDetail.iat = iat;
            return this;
        }

        public Builder setExp(Long exp) {
            this.jwtUserDetail.exp = exp;
            return this;
        }

        public Builder setRoles(Collection<? extends GrantedAuthority> roles) {
            this.jwtUserDetail.roles = roles;
            return this;
        }

        public Builder setId(UUID id) {
            this.jwtUserDetail.id = id;
            return this;
        }

        public Builder setCn(String cn) {
            this.jwtUserDetail.cn = cn;
            return this;
        }

        public Builder setPassword(String password) {
            this.jwtUserDetail.password = password;
            return this;
        }

        public JWTUserDetail build() {
            return this.jwtUserDetail;
        }
    }
}
