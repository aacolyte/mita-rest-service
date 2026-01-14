package com.mita.controller;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.service.ItemService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping
    public ItemContainerDto getAllItems(){
        return itemService.getAllItems();
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
    public ItemDto updateItem(@Valid @PathVariable Long id, @RequestBody ItemUpdateRequest request) {
        return itemService.updateItem(id,request);
    }


    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id) {
        itemService.deleteItemById(id);
    }




    @GetMapping("/by-title")
    public ItemContainerDto getItemByTitle(@RequestParam Long categoryId, @RequestParam String title) {
        return itemService.getItemsByTitle(categoryId, title);
    }


    @GetMapping("by-rating")
    public ItemContainerDto getItemByRating(@RequestParam Long categoryId, @RequestParam Double rating) {
        return itemService.getItemsByRating(categoryId, rating);
    }

    @GetMapping("/rating-above")
    public ItemContainerDto getItemsWithRatingGreaterThan(@RequestParam Long categoryId, @RequestParam Double rating) {
        return itemService.getItemsWithRatingGreaterThan(categoryId, rating);
    }

    @GetMapping("/top")
    public ItemContainerDto getTopItemsByRating(@RequestParam Long categoryId, @RequestParam Integer limit) {
        return itemService.getTopItemsByRating(categoryId, limit);
    }



}
