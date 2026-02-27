package com.mita.service;

import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.dto.request.category.CategoryCreateRequest;
import com.mita.dto.request.category.CategoryUpdateRequest;
import com.mita.entity.Category;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final UploadService uploadService;
    private final CategoryRepository categoryRepository;
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository, UserService userService, ItemRepository itemRepository, UploadService uploadService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
        this.uploadService = uploadService;
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

        Category saved = categoryRepository.save(category);

        log.info("Created category {} for user {}", saved.getId(), currentUser.getId());

        return saved.toDto();
    }

    public CategoryDto updateCategory(Long id, CategoryUpdateRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(id,user).orElseThrow(
                ()-> new IllegalArgumentException("Category with id: "+ id +" not found"));

        request.applyTo(category);

        Category saved = categoryRepository.save(category);

        log.info("Updated category {} for user {}", saved.getId(), user.getId());

        return saved.toDto();
    }

    public void deleteCategoryById(Long id) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(id,user).orElseThrow(()->
            new IllegalArgumentException("Category with id: " + id + " not found"));

        deleteCategoryPosters(id);

        categoryRepository.delete(category);

        log.info("Deleted category {} for user {}", id, user.getId());
    }

    private void deleteCategoryPosters(Long id) {
        int page = 0;
        Page<String> posterPage;

        do {
            posterPage = itemRepository.findPosterNameByCategoryId(
                    id,
                    PageRequest.of(page, 100)
            );
            for (String poster : posterPage.getContent()) {
                uploadService.deletePosterIfExists(poster);
            }
            page++;

        }while(!posterPage.isLast());
    }




}
