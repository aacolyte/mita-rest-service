package com.mita.dto.request.user;

public class AvatarUpdateRequest {

    private String avatar;

    public AvatarUpdateRequest(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

}
