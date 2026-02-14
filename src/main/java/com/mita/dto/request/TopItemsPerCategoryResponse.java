package com.mita.dto.request;

import com.mita.dto.ItemDto;

public class TopItemsPerCategoryResponse {

    private Long categoryId;
    private String categoryName;
    private ItemDto topItem;

    public TopItemsPerCategoryResponse(Long categoryId, String categoryName, ItemDto topItem) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.topItem = topItem;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public ItemDto getTopItem() {
        return topItem;
    }
}
