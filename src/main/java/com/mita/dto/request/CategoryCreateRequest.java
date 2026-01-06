package com.mita.dto.request;

import com.mita.entity.Category;
import com.mita.entity.Item;

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
