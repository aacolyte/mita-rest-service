package com.mita.service;

import com.mita.dto.CategoryContainerDto;
import com.mita.dto.CategoryDto;
import com.mita.dto.request.category.CategoryCreateRequest;
import com.mita.dto.request.category.CategoryUpdateRequest;
import com.mita.entity.Category;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryCreateRequest request;

    private Category savedCategory;

    private User user;


    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(userService.getCurrentUser()).thenReturn(user);

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

        verify(userService, times(1)).getCurrentUser();
        verify(categoryRepository,times(1)).save(any(Category.class));
    }

    @Test
    void shouldReturnCategoryById_WhenCategoryExists() {
        when(categoryRepository.findByIdAndUser(1L,user)).thenReturn(Optional.of(savedCategory));

        CategoryDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Anime",result.getName());

        verify(userService, times(1)).getCurrentUser();
        verify(categoryRepository,times(1)).findByIdAndUser(1L,user);
    }

    @Test
    void shouldThrowException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findByIdAndUser(99L,user)).thenReturn(Optional.empty());

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

        when(categoryRepository.findByUser(user)).thenReturn(List.of(category1, category2));

        CategoryContainerDto result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.getAllCategories().size());
        assertEquals("Anime",result.getAllCategories().get(0).getName());
        assertEquals("Movie",result.getAllCategories().get(1).getName());

        verify(userService, times(1)).getCurrentUser();
        verify(categoryRepository,times(1)).findByUser(user);
    }
    @Test
    void shouldUpdateCategory_whenCategoryExists() {
        CategoryUpdateRequest request = new CategoryUpdateRequest("New name");

        when(categoryRepository.findByIdAndUser(1L,user)).thenReturn(Optional.of(savedCategory));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));


        CategoryDto result = categoryService.updateCategory(1L, request);

        assertNotNull(result);
        assertEquals("New name",result.getName());
        assertEquals("New name",savedCategory.getName());

        verify(userService, times(1)).getCurrentUser();
        verify(categoryRepository, times(1)).findByIdAndUser(1L,user);
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void shouldDeleteCategory_whenCategoryExists() {
        when(categoryRepository.findByIdAndUser(1L,user)).thenReturn(Optional.of(savedCategory));
        when(itemRepository.findPosterNameByCategoryId(eq(1L), eq(PageRequest.of(0, 100)))).thenReturn(Page.empty());

        categoryService.deleteCategoryById(1L);

        verify(userService, times(1)).getCurrentUser();
        verify(categoryRepository, times(1)).delete(savedCategory);
    }









}
