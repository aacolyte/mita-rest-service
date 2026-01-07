package com.mita.service;

import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.dto.request.CategoryCreateRequest;
import com.mita.dto.request.CategoryUpdateRequest;
import com.mita.entity.Category;
import com.mita.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryContainerDto getAllCategories() {
        List<CategoryDto> categoryList = categoryRepository.findAll().stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
        return new CategoryContainerDto(categoryList);
    }

    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(Category::toDto)
                .orElseThrow(()-> new IllegalArgumentException("Category with id: "+ id +" not found"));
    }

    public CategoryDto createCategory(CategoryCreateRequest request) {
        Category category = request.toEntity();
        return categoryRepository.save(category).toDto();
    }

    public CategoryDto updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Category with id: "+ id +" not found"));
        request.applyTo(category);
        return category.toDto();

    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }


}
