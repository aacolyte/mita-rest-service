package com.mita.controller;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.item.ItemCreateRequest;
import com.mita.dto.request.item.ItemUpdateRequest;
import com.mita.dto.request.item.TopItemsPerCategoryResponse;
import com.mita.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }



    @GetMapping
    public ResponseEntity<ItemContainerDto> getItems(
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
        return ResponseEntity.ok(itemService.getItems(
                categoryId,
                title,
                rating,
                ratingAbove,
                additionalInfo,
                limit,
                page,
                size,
                sort
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id){
        return ResponseEntity.ok(itemService.getItemById(id));
    }


    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemCreateRequest request) {
        return ResponseEntity.ok(itemService.createItem(request));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemUpdateRequest request) {
        return ResponseEntity.ok(itemService.updateItem(id,request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable Long id) {
        itemService.deleteItemById(id);

        return ResponseEntity.noContent().build();
    }



    @GetMapping("/top-items")
    public ResponseEntity<List<TopItemsPerCategoryResponse>> topItemsPerCategory(){
        return ResponseEntity.ok(itemService.getTopItemsPerCategory());
    }





}
