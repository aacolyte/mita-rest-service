package com.mita.controller;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.service.ItemService;
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
    @GetMapping
    public ItemDto getItemById(@PathVariable Long id){
        return itemService.getItemById(id);
    }

    @ResponseBody
    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto);
    }

    @ResponseBody
    @PutMapping("")
    public ItemDto updateItem(@PathVariable Long id, @RequestBody ItemUpdateRequest request) {
        return itemService.updateItem(id,request);
    }


}
