package com.mita.service;


import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.entity.Category;
import com.mita.entity.Item;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    private final CategoryRepository categoryRepository;
    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
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

    public ItemDto createItem(ItemCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                ()-> new IllegalArgumentException("Category with id: "+ request.getCategoryId() +" not found")
        );
        Item item = request.toEntity(category);
        return itemRepository.save(item).toDto();
    }

    public ItemDto updateItem(Long id, ItemUpdateRequest request) {
        Item item = itemRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Item with id: "+ id +" not found"));
        request.applyTo(item);
        return item.toDto();
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("Item with id: " + id + " not found");
        }

        itemRepository.deleteById(id);
    }

}
