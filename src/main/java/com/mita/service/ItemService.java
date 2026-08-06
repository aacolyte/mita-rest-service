package com.mita.service;


import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.item.ItemCreateRequest;
import com.mita.dto.request.item.ItemUpdateRequest;
import com.mita.dto.request.item.TopItemsPerCategoryResponse;
import com.mita.entity.Category;
import com.mita.entity.Item;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import com.mita.specification.ItemSpecification;
import com.mita.exception.ValidationException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final UploadService uploadService;
    private static final Logger log = LoggerFactory.getLogger(ItemService.class);


    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, UserService userService, UploadService uploadService) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.uploadService = uploadService;
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

        return Sort.by(
                new Sort.Order(direction, field),
                new Sort.Order(Sort.Direction.ASC, "id")
        );
    }



    public ItemDto getItemById(Long id) {
        User user = userService.getCurrentUser();
        return itemRepository.findByIdAndUser(id,user)
                .map(Item::toDto)
                .orElseThrow(()-> new IllegalArgumentException("Item with id: "+ id +" not found"));
    }

    public ItemDto createItem(ItemCreateRequest request) {
        User user = userService.getCurrentUser();

        try {
            validateItemRequest(request.getRating());

            Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user).orElseThrow(
                    () -> new IllegalArgumentException("Category with id: " + request.getCategoryId() + " not found")
            );

            Item item = request.toEntity(category);
            item.setUser(user);

            Item saved  = itemRepository.save(item);

            log.info("Created item {} for user {}", saved.getId(), user.getId());
            return saved.toDto();

        }catch (ValidationException e) {
            if(request.getPoster() != null) {
                uploadService.deletePosterIfExists(request.getPoster());
            }
            throw e;
        }
    }

    public ItemDto updateItem(Long id, ItemUpdateRequest request) {
        User user = userService.getCurrentUser();
        Item item = itemRepository.findByIdAndUser(id,user).orElseThrow(()-> new IllegalArgumentException("Item with id: "+ id +" not found"));

        String oldPoster = item.getPoster();
        try {
            validateItemRequest(request.getRating());

            request.applyTo(item);

            if (oldPoster != null && !oldPoster.equals(item.getPoster())) {
                uploadService.deletePosterIfExists(oldPoster);
            }

            Item saved = itemRepository.save(item);

            log.info("Updated item {} for user {}", saved.getId(), user.getId());
            return saved.toDto();

        }catch (ValidationException e) {
            if(request.getPoster() != null &&
            oldPoster != null &&
            !oldPoster.equals(item.getPoster())) {
                uploadService.deletePosterIfExists(request.getPoster());
            }
            throw e;
        }
    }

    public void deleteItemById(Long id) {
        User user = userService.getCurrentUser();
        Item item = itemRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new IllegalArgumentException("Item with id: " + id + " not found"));

        uploadService.deletePosterIfExists(item.getPoster());
        itemRepository.deleteById(id);

        log.info("Deleted item {} for user {}", id, user.getId());
    }




    public List<TopItemsPerCategoryResponse> getTopItemsPerCategory(String username) {
        User user;

        if(username != null && !username.isBlank()){
            user = userService.getUserByUsernamePrivate(username);
        }else {
            user = userService.getCurrentUser();
        }


        List<Item> topItems =
                itemRepository.findTopItemsPerCategory(user.getId());

        return topItems.stream()
                .map(item -> new TopItemsPerCategoryResponse(
                        item.getCategory().getId(),
                        item.getCategory().getName(),
                        item.toDto()
                )).toList();
    }



    private void validateItemRequest(Double rating) {
        if(rating == null ) {
            throw new ValidationException("Rating must not be null");
        }

        if(rating < 0 || rating > 10){
            throw new ValidationException("Rating must be between 0 and 10");
        }

    }






}
