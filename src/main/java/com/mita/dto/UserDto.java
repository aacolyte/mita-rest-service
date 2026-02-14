package com.mita.dto;

public class UserDto {
    String username;
    String email;
    String avatar;
    String about;

    public UserDto(String email, String username, String avatar, String about) {
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        this.about = about;
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
    public String getAbout() {
        return about;
    }
}
