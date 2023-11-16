package com.proyecto.integrador.enums;

import lombok.Getter;

@Getter
public enum Role {
    SUPERADMIN("Super-Admin"),
    ADMIN("Admin"),
    USER("User");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}