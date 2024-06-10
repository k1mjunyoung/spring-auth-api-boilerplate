package SpringAuthApiBoilerplate.utils;

import lombok.Getter;

@Getter
public enum CustomUserRole {
    ADMIN("AMDIN"),
    USER("USER");

    CustomUserRole(String role) {
        this.role = role;
    }

    private String role;
}
