package com.mita.dto.request.user;

public class AboutUpdateRequest {
    private String about;

    public AboutUpdateRequest(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }
}
