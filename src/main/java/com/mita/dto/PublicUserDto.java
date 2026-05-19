package com.mita.dto;

public class PublicUserDto {
    private final String username;
    private final String avatar;
    private final String about;

    private final long followers;
    private final long following;

    public PublicUserDto(String username, String avatar, String about, long followers, long following) {
        this.username = username;
        this.avatar = avatar;
        this.about = about;
        this.followers = followers;
        this.following = following;
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

    public long getFollowers() {
        return followers;
    }

    public long getFollowing() {
        return following;
    }
}
