package com.mita.dto.request.category;

import com.mita.entity.Category;

public class CategoryCreateRequest {
    private String name;

    public CategoryCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Category toEntity() {
        return new Category(
                this.getName()
        );
    }
}
