package com.mita.service;


import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.dto.request.TopItemsPerCategoryResponse;
import com.mita.entity.Category;
import com.mita.entity.Item;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import com.mita.specification.ItemSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class ItemService {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, CategoryService categoryService, UserService userService) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    public ItemContainerDto getItems(
            Long categoryId,
            String title,
            Double rating,
            Double ratingAbove,
            String additionalInfo,
            Integer limit,
            Integer page,
            Integer size,
            String sort
    ) {
        User user = userService.getCurrentUser();
        Sort sorting = parseSort(sort);

        Specification<Item> spec = ItemSpecification.withFilters(
                categoryId,
                user.getId(),
                title,
                rating,
                ratingAbove,
                additionalInfo
        );


        int safePage =  (page == null || page < 0) ? 0 : page;
        int safeSize = (size == null || size <= 0) ? 12 : Math.min(size,12);

        Pageable pageable = PageRequest.of(safePage, safeSize, sorting);

        Page<Item> pageResult = itemRepository.findAll(spec, pageable);

        List<ItemDto> items = pageResult.getContent()
                    .stream().map(Item::toDto)
                    .toList();


        return new ItemContainerDto(items, pageResult.getTotalPages(), pageResult.getTotalElements());
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
        User user = userService.getCurrentUser();
        return itemRepository.findByIdAndUser(id,user)
                .map(Item::toDto)
                .orElseThrow(()-> new IllegalArgumentException("Item with id: "+ id +" not found"));
    }

    public ItemDto createItem(ItemCreateRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(),user).orElseThrow(
                ()-> new IllegalArgumentException("Category with id: "+ request.getCategoryId() +" not found")
        );
        Item item = request.toEntity(category);
        item.setUser(user);
        return itemRepository.save(item).toDto();
    }

    public ItemDto updateItem(Long id, ItemUpdateRequest request) {
        User user = userService.getCurrentUser();
        Item item = itemRepository.findByIdAndUser(id,user).orElseThrow(()-> new IllegalArgumentException("Item with id: "+ id +" not found"));
        request.applyTo(item);
        return item.toDto();
    }

    public void deleteItemById(Long id) {
        User user = userService.getCurrentUser();
        Item item = itemRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new IllegalArgumentException("Item with id: " + id + " not found"));

        deletePosterIfExists(item.getPoster());
        itemRepository.deleteById(id);
    }

    public void deletePosterIfExists(String poster){

        if(poster == null || poster.isBlank()) return;
        try{
            Path filePath = Paths.get("posters").toAbsolutePath().resolve(poster).normalize();
            if(!filePath.startsWith(filePath)){
                return;
            }
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete poster: " + poster);
        }
    }


    public void deletePoster(String poster) {
        User user = userService.getCurrentUser();

        boolean ownsPoster =
                itemRepository.exists(
                        (root,q,cb) ->
                                cb.and(
                                        cb.equal(root.get("poster"), poster),
                                        cb.equal(root.get("user"), user)
                                )
                );
        if(!ownsPoster){
            throw new AccessDeniedException("Not your poster");
        }
        deletePosterIfExists(poster);
    }



    public List<TopItemsPerCategoryResponse> getTopItemsPerCategory() {
        User user = userService.getCurrentUser();

        List<Item> topItems =
                itemRepository.findTopItemsPerCategory(user.getId());

        return topItems.stream()
                .map(item -> new TopItemsPerCategoryResponse(
                        item.getCategory().getId(),
                        item.getCategory().getName(),
                        item.toDto()
                )).toList();
    }






}
