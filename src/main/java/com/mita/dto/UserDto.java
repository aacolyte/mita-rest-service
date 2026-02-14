package com.mita.dto;

public class UserDto {
    String username;
    String email;
    String avatar;

    public UserDto(String email, String username, String avatar) {
        this.email = email;
        this.username = username;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }
}
