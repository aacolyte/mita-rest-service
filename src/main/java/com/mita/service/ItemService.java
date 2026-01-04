package com.mita.service;


import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.entity.Item;
import com.mita.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public ItemContainerDto getAllItems() {
        List<ItemDto> itemList = itemRepository.findAll().stream()
                .map(Item::toDto)
                .collect(Collectors.toList());
        return new ItemContainerDto(itemList);
    }

    public ItemDto getItemById(Long id) {
        return itemRepository.findById(id)
                .map(Item::toDto)
                .orElseThrow(()-> new IllegalArgumentException("Item with id: "+ id +" not found"));
    }




}
