package com.mita.controller;


import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.dto.request.category.CategoryCreateRequest;
import com.mita.dto.request.category.CategoryUpdateRequest;
import com.mita.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CategoryContainerDto> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }
    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);

        return ResponseEntity.noContent().build();
    }



}
