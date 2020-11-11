package com.sadeqstore.demo.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_NORMAL,ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
