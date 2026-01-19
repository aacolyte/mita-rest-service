package com.mita.service;


import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.entity.Category;
import com.mita.entity.Item;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import com.mita.specification.ItemSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, CategoryService categoryService) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }


    public ItemContainerDto getItems(
            Long categoryId,
            String title,
            Double rating,
            Double ratingAbove,
            String additionalInfo,
            Integer limit,
            String sort
    ) {
        Sort sorting = parseSort(sort);

        Specification<Item> spec = ItemSpecification.withFilters(
                categoryId,
                title,
                rating,
                ratingAbove,
                additionalInfo
        );
        List<ItemDto> items;

        int safeLimit = (limit == null || limit <= 0)
            ? 50
            : Math.min(limit, 50);

            Pageable pageable = PageRequest.of(0, safeLimit, sorting);
            items = itemRepository.findAll(spec, pageable)
                    .stream().map(Item::toDto)
                    .toList();


        return new ItemContainerDto(items);
    }

    private Sort parseSort(String sort) {
        String[] parts = sort.split(",");

        String field = parts[0];
        Sort.Direction direction =
                parts.length > 1 && parts[1].equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        return Sort.by(direction, field);
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

    public void deleteItemById(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new IllegalArgumentException("Item with id: " + id + " not found");
        }

        itemRepository.deleteById(id);
    }









}
