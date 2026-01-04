package com.mita.service;

import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.entity.Category;
import com.mita.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

}
