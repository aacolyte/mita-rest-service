package com.mita.dto;



import java.util.List;

public class ItemContainerDto {

    private final List<ItemDto> itemList;

    public ItemContainerDto(List<ItemDto> itemList) {
        this.itemList = itemList;
    }

    public List<ItemDto> getItemList() {
        return itemList;
    }
}
