package com.mita.dto;

public class PublicUserDto {
    private final String username;
    private final String avatar;
    private final String about;

    public PublicUserDto(String username, String avatar, String about) {
        this.username = username;
        this.avatar = avatar;
        this.about = about;
    }

    public String getUsername() {
        return username;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getAbout() {
        return about;
    }
}
