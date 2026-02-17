package com.mita.controller;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.dto.request.TopItemsPerCategoryResponse;
import com.mita.service.ItemService;
import com.mita.service.UploadService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final UploadService uploadService;
    private ItemService itemService;

    public ItemController(ItemService itemService, UploadService uploadService) {
        this.itemService = itemService;
        this.uploadService = uploadService;
    }



    @GetMapping
    public ItemContainerDto getItems(
            @RequestParam Long categoryId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Double ratingAbove,
            @RequestParam(required = false) String additionalInfo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer limit,

            @RequestParam(required = false, defaultValue = "rating,desc") String sort
            ) {
        return itemService.getItems(
                categoryId,
                title,
                rating,
                ratingAbove,
                additionalInfo,
                limit,
                page,
                size,
                sort
        );
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id){
        return itemService.getItemById(id);
    }


    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemCreateRequest request) {
        return itemService.createItem(request);
    }


    @PutMapping("/{id}")
    public ItemDto updateItem(@PathVariable Long id, @Valid @RequestBody ItemUpdateRequest request) {
        return itemService.updateItem(id,request);
    }


    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id) {
        itemService.deleteItemById(id);
    }


    @PostMapping("/poster/delete")
    public void deletePoster(@RequestBody Map<String, String> body){
        String poster = body.get("poster");
        if(poster != null){
            uploadService.deletePoster(poster);
        }
    }

    @GetMapping("/top-items")
    public List<TopItemsPerCategoryResponse> topItemsPerCategory(){
        return itemService.getTopItemsPerCategory();
    }





}
