package com.mita.dto.request.user;

public class NameUpdateRequest {
    private String name;

    public NameUpdateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
