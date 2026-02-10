package com.mita.dto;

public class UserDto {
    String username;
    String email;

    public UserDto(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
