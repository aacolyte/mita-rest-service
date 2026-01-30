package com.mita.controller;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.service.ItemService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }



    @GetMapping
    public ItemContainerDto getItems(
            @RequestParam Long categoryId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Double ratingAbove,
            @RequestParam(required = false) String additionalInfo,
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
            itemService.deletePosterIfExists(poster);
        }
    }





}
