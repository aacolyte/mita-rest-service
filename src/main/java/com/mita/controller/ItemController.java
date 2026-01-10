package com.mita.controller;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseBody
    @GetMapping
    public ItemContainerDto getAllItems(){
        return itemService.getAllItems();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id){
        return itemService.getItemById(id);
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemCreateRequest request) {
        return itemService.createItem(request);
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ItemDto updateItem(@Valid @PathVariable Long id, @RequestBody ItemUpdateRequest request) {
        return itemService.updateItem(id,request);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable Long id) {
        itemService.deleteItemById(id);
    }



    @ResponseBody
    @GetMapping
    public void getItemByTitle(@RequestParam Long categoryId, @RequestParam String title) {
        itemService.getItemsByTitle(categoryId, title);
    }

}
