package com.mita.controller;


import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.dto.request.CategoryCreateRequest;
import com.mita.dto.request.CategoryUpdateRequest;
import com.mita.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ResponseBody
    @GetMapping
    public CategoryContainerDto getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @ResponseBody
    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryCreateRequest request) {
        return categoryService.createCategory(request);
    }
    @ResponseBody
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }



}
