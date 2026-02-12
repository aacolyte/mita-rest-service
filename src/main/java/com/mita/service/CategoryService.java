package com.mita.service;

import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.dto.request.CategoryCreateRequest;
import com.mita.dto.request.CategoryUpdateRequest;
import com.mita.entity.Category;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private final UserService userService;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public CategoryContainerDto getAllCategories() {
        User user = userService.getCurrentUser();
        List<CategoryDto> categoryList = categoryRepository.findByUser(user).stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
        return new CategoryContainerDto(categoryList);
    }

    public CategoryDto getCategoryById(Long id) {
        User user = userService.getCurrentUser();
        return categoryRepository.findByIdAndUser(id,user)
                .map(Category::toDto)
                .orElseThrow(()-> new IllegalArgumentException("Category with id: "+ id +" not found"));
    }

    public CategoryDto createCategory(CategoryCreateRequest request) {
        Category category = request.toEntity();
        User currentUser = userService.getCurrentUser();
        category.setUser(currentUser);
        return categoryRepository.save(category).toDto();
    }

    public CategoryDto updateCategory(Long id, CategoryUpdateRequest request) {
        User user = userService.getCurrentUser();
        Category category = categoryRepository.findByIdAndUser(id,user).orElseThrow(
                ()-> new IllegalArgumentException("Category with id: "+ id +" not found"));
        request.applyTo(category);
        return category.toDto();

    }

    public void deleteCategoryById(Long id) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(id,user).orElseThrow(()->
            new IllegalArgumentException("Category with id: " + id + " not found"));

        categoryRepository.delete(category);
    }


}
