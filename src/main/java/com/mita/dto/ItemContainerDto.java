package com.mita.dto;



import java.util.List;

public class ItemContainerDto {

    private final List<ItemDto> itemList;
    private int totalPages;
    private long totalElements;

    public ItemContainerDto(List<ItemDto> itemList, int totalPages, long totalElements) {
        this.itemList = itemList;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<ItemDto> getAllItems() {
        return itemList;
    }
}
