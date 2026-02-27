package com.mita.dto.request.category;

import com.mita.entity.Category;

public class CategoryUpdateRequest {
    private String name;

    public CategoryUpdateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void applyTo(Category category) {
        category.setName(name);
    }
}
