package com.mita.dto;


import java.util.List;

public class CategoryContainerDto {

    private final List<CategoryDto> categoryList;

    public CategoryContainerDto(List<CategoryDto> categoryList) {
        this.categoryList = categoryList;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryList;
    }
}
