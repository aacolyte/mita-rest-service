package com.mita.controller;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseBody
    @GetMapping("/items")
    public ItemContainerDto getAllItems(){
        return itemService.getAllItems();
    }

    @ResponseBody
    @GetMapping("/items/{id}")
    public ItemDto getItemById(@PathVariable Long id){
        return itemService.getItemById(id);
    }

    @ResponseBody
    @PostMapping("/items")
    public ItemDto createItem(@RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto);
    }


}
