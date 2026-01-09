package com.mita.service;

import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.dto.request.CategoryCreateRequest;
import com.mita.dto.request.CategoryUpdateRequest;
import com.mita.entity.Category;
import com.mita.repository.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    CategoryCreateRequest request;
    Category savedCategory;

    @BeforeEach
    public void setUp() {
        savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Anime");
    }

    @Test
    void shouldCreateCategory() {
        request = new CategoryCreateRequest("Anime");

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory);

        CategoryDto result = categoryService.createCategory(request);

        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Anime",result.getName());
        verify(categoryRepository,times(1)).save(any(Category.class));
    }

    @Test
    void shouldReturnCategoryById_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(savedCategory));

        CategoryDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Anime",result.getName());
        verify(categoryRepository,times(1)).findById(1L);
    }

    @Test
    void shouldThrowException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.getCategoryById(99L)
        );
        assertEquals("Category with id: "+99+ " not found", ex.getMessage());
    }


    @Test
    void shouldReturnAllCategories() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Anime");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Movie");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        CategoryContainerDto result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.getAllCategories().size());
        assertEquals("Anime",result.getAllCategories().get(0).getName());
        assertEquals("Movie",result.getAllCategories().get(1).getName());
        verify(categoryRepository,times(1)).findAll();
    }
    @Test
    void shouldUpdateCategory_whenCategoryExists() {
        CategoryUpdateRequest request = new CategoryUpdateRequest("New name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(savedCategory));

        CategoryDto result = categoryService.updateCategory(1L, request);

        assertNotNull(result);
        assertEquals("New name",result.getName());
        assertEquals("New name",savedCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldDeleteCategory_whenCategoryExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        categoryService.deleteCategoryById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }









}
