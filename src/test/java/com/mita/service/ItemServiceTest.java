package com.mita.service;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.entity.Category;
import com.mita.entity.Item;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UploadService uploadService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemService itemService;


    private ItemCreateRequest request;
    private Item savedItem;
    private Category savedCategory;

    private User user;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(userService.getCurrentUser()).thenReturn(user);

        savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Anime");

        savedItem = new Item();
        savedItem.setTitle("Some game");
        savedItem.setId(1L);
        savedItem.setRating(10.0);
        savedItem.setAdditionalInfo("true");
        savedItem.setCategory(savedCategory);

        savedItem.setUser(user);
    }

    @Test
    void shouldCreateItem(){
        request = new ItemCreateRequest("Some game", 10.0, "true", 1L, "some path");

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(savedCategory));

        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        ItemDto result = itemService.createItem(request);

        assertNotNull(result);
        assertEquals(1L,result.getCategoryId());
        assertEquals("Some game",result.getTitle());
        assertEquals("true",result.getAdditionalInfo());
        assertEquals(10.0,result.getRating());
        verify(itemRepository, times(1)).save(any(Item.class));
    }
    @Test
    void shouldReturnItemById_WhenItemExists() {
        when(itemRepository.findByIdAndUser(1L,user)).thenReturn(Optional.of(savedItem));

        ItemDto result = itemService.getItemById(1L);

        assertNotNull(result);
        assertEquals(1L,result.getCategoryId());
        assertEquals("Some game",result.getTitle());
        assertEquals("true",result.getAdditionalInfo());
        assertEquals(10.0,result.getRating());
        verify(itemRepository, times(1)).findByIdAndUser(1L,user);
    }
    @Test
    void shouldThrowException_WhenItemDoesNotExist() {
        when(itemRepository.findByIdAndUser(99L,user)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> itemService.getItemById(99L)
        );
        assertEquals("Item with id: " + 99L + " not found", ex.getMessage());
    }

    @Test
    void shouldUpdateItem_whenItemExists() {
        ItemUpdateRequest request = new ItemUpdateRequest("New title",7.5,"false", "some path");

        when(itemRepository.findByIdAndUser(1L,user)).thenReturn(Optional.of(savedItem));

        ItemDto result = itemService.updateItem(1L,request);

        assertNotNull(result);
        assertEquals(1L,result.getCategoryId());
        assertEquals("New title",result.getTitle());
        assertEquals("false",result.getAdditionalInfo());
        assertEquals(7.5,result.getRating());
        verify(itemRepository, times(1)).findByIdAndUser(1L,user);
        verify(itemRepository, never()).save(any());
    }
    @Test
    void shouldDeleteItem_whenItemExist() {
        when(itemRepository.findByIdAndUser(1L,user)).thenReturn(Optional.of(savedItem));
        itemService.deleteItemById(1L);
        verify(itemRepository,times(1)).deleteById(1L);
    }




    @Test
    void shouldReturnItemsFilteredByTitle() {
        Item item2 = new Item();
        item2.setId(2L);
        item2.setTitle("Game 2");
        item2.setRating(7.0);
        item2.setAdditionalInfo("false");
        item2.setCategory(savedCategory);

        when(itemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(savedItem, item2)));

        ItemContainerDto result = itemService.getItems(
                1L, "game", null, null, null, 2,1,1,"rating,desc");


        assertNotNull(result);
        assertEquals(2, result.getAllItems().size());
        assertTrue(result.getAllItems().get(0).getRating() >= result.getAllItems().get(1).getRating());
    }

    @Test
    void shouldReturnItemsFilteredByRatingAbove() {
        Item item2 = new Item();
        item2.setId(2L);
        item2.setTitle("Game 2");
        item2.setRating(10.0);
        item2.setAdditionalInfo("false");
        item2.setCategory(savedCategory);

        when(itemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(savedItem, item2)));

        ItemContainerDto result = itemService.getItems(
                1L, null, null, 9.0, null, 2,1,1,"rating,desc");

        assertNotNull(result);
        assertEquals(2, result.getAllItems().size());
        assertTrue(result.getAllItems().stream().allMatch(i -> i.getRating() >= 9.0));
    }

    @Test
    void shouldReturnItemsFilteredByAdditionalInfo() {
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(savedItem)));

        ItemContainerDto result = itemService.getItems(
                1L, null, null, null, "true",1,1,1, "rating,desc");

        assertNotNull(result);
        assertEquals(1, result.getAllItems().size());
        assertEquals("true", result.getAllItems().get(0).getAdditionalInfo());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatch() {
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        ItemContainerDto result = itemService.getItems(
                1L, "nonexistent", null, null, null,1,1,1, "rating,desc");

        assertNotNull(result);
        assertTrue(result.getAllItems().isEmpty());
    }














}









