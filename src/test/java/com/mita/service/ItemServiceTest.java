package com.mita.service;

import com.mita.dto.ItemContainerDto;
import com.mita.dto.ItemDto;
import com.mita.dto.request.ItemCreateRequest;
import com.mita.dto.request.ItemUpdateRequest;
import com.mita.entity.Category;
import com.mita.entity.Item;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private ItemService itemService;


    ItemCreateRequest request;
    Item savedItem;
    Category savedCategory;

    @BeforeEach
    public void setUp(){
        savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Anime");

        savedItem = new Item();
        savedItem.setTitle("Some game");
        savedItem.setId(1L);
        savedItem.setRating(10.0);
        savedItem.setAdditionalInfo("true");
        savedItem.setCategory(savedCategory);
    }

    @Test
    void shouldCreateItem(){
        request = new ItemCreateRequest("Some game", 10.0, "true", 1L);

        when(categoryRepository.findById(1L))
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
        when(itemRepository.findById(1L)).thenReturn(Optional.of(savedItem));

        ItemDto result = itemService.getItemById(1L);

        assertNotNull(result);
        assertEquals(1L,result.getCategoryId());
        assertEquals("Some game",result.getTitle());
        assertEquals("true",result.getAdditionalInfo());
        assertEquals(10.0,result.getRating());
        verify(itemRepository, times(1)).findById(1L);
    }
    @Test
    void shouldThrowException_WhenItemDoesNotExist() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> itemService.getItemById(99L)
        );
        assertEquals("Item with id: " + 99L + " not found", ex.getMessage());
    }

    @Test
    void shouldReturnAllItems(){
        Item item1 = new Item();
        item1.setTitle("Some game1");
        item1.setId(1L);
        item1.setRating(10.0);
        item1.setAdditionalInfo("true");
        item1.setCategory(savedCategory);

        Item item2 = new Item();
        item2.setTitle("Some game2");
        item2.setId(2L);
        item2.setRating(7.5);
        item2.setAdditionalInfo("false");
        item2.setCategory(savedCategory);

        when(itemRepository.findAll()).thenReturn(List.of(item1,item2));

        ItemContainerDto result = itemService.getAllItems();

        assertNotNull(result);
        assertEquals(2,result.getAllItems().size());

        assertEquals("Some game1",result.getAllItems().get(0).getTitle());
        assertEquals("true",result.getAllItems().get(0).getAdditionalInfo());
        assertEquals(10.0,result.getAllItems().get(0).getRating());

        assertEquals("Some game2",result.getAllItems().get(1).getTitle());
        assertEquals("false",result.getAllItems().get(1).getAdditionalInfo());
        assertEquals(7.5,result.getAllItems().get(1).getRating());

        verify(itemRepository, times(1)).findAll();
    }
    @Test
    void shouldUpdateItem_whenItemExists() {
        ItemUpdateRequest request = new ItemUpdateRequest("New title",7.5,"false");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(savedItem));

        ItemDto result = itemService.updateItem(1L,request);

        assertNotNull(result);
        assertEquals(1L,result.getCategoryId());
        assertEquals("New title",result.getTitle());
        assertEquals("false",result.getAdditionalInfo());
        assertEquals(7.5,result.getRating());
        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, never()).save(any());
    }
    @Test
    void shouldDeleteItem_whenItemExist() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        itemService.deleteItemById(1L);
        verify(itemRepository,times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnItemsByTitle(){
        Item item2 = new Item();
        item2.setTitle("Some game2");
        item2.setId(2L);
        item2.setRating(7.5);
        item2.setAdditionalInfo("false");
        item2.setCategory(savedCategory);

        Item item3 = new Item();
        item3.setTitle("game2");
        item3.setId(3L);
        item3.setRating(8.0);
        item3.setAdditionalInfo("false");
        item3.setCategory(savedCategory);

        when(itemRepository.findByCategoryIdAndTitleContainingIgnoreCase(1L,"2"))
                .thenReturn(List.of(item2, item3));

        ItemContainerDto result = itemService.getItemsByTitle(1L,"2");

        assertNotNull(result);
        assertEquals(2,result.getAllItems().size());

        assertEquals("Some game2",result.getAllItems().get(0).getTitle());
        assertEquals("false",result.getAllItems().get(0).getAdditionalInfo());
        assertEquals(7.5,result.getAllItems().get(0).getRating());

        assertEquals("game2",result.getAllItems().get(1).getTitle());
        assertEquals("false",result.getAllItems().get(1).getAdditionalInfo());
        assertEquals(8.0,result.getAllItems().get(1).getRating());

        verify(itemRepository, times(1)).findByCategoryIdAndTitleContainingIgnoreCase(1L,"2");
    }
    @Test
    void shouldReturnEmptyList_whenNoItemsFoundByTitle() {
        when(itemRepository.findByCategoryIdAndTitleContainingIgnoreCase(1L,"game"))
                .thenReturn(List.of());

        ItemContainerDto result = itemService.getItemsByTitle(1L,"game");

        assertNotNull(result);
        assertTrue(result.getAllItems().isEmpty());
    }

    @Test
    void shouldReturnItemByRating() {
        Item item2 = new Item();
        item2.setTitle("Some game2");
        item2.setId(2L);
        item2.setRating(7.5);
        item2.setAdditionalInfo("false");
        item2.setCategory(savedCategory);

        when(itemRepository.findByCategoryIdAndRating(1L,7.5))
                .thenReturn((List.of(item2)));

        ItemContainerDto result = itemService.getItemsByRating(1L,7.5);

        assertNotNull(result);
        assertEquals(1,result.getAllItems().size());

        assertEquals("Some game2",result.getAllItems().get(0).getTitle());
        assertEquals("false",result.getAllItems().get(0).getAdditionalInfo());
        assertEquals(7.5,result.getAllItems().get(0).getRating());

        verify(itemRepository, times(1)).findByCategoryIdAndRating(1L,7.5);
    }

    @Test
    void shouldReturnEmptyList_whenNoItemsFoundByRating() {
        when(itemRepository.findByCategoryIdAndRating(1L,7.5))
                .thenReturn(List.of());

        ItemContainerDto result = itemService.getItemsByRating(1L,7.5);

        assertNotNull(result);
        assertTrue(result.getAllItems().isEmpty());
    }
    @Test
    void shouldReturnAllItemsByCategory_whenRatingIsNull() {
        when(itemRepository.findByCategoryId(1L))
                .thenReturn(List.of(savedItem));

        ItemContainerDto result = itemService.getItemsByRating(1L, null);

        assertEquals(1, result.getAllItems().size());

        verify(itemRepository, times(1)).findByCategoryId(1L);
        verify(itemRepository, never()).findByCategoryIdAndRating(any(), any());
    }

    @Test
    void shouldReturnItemsWithRatingGreaterThan() {
        Item item2 = new Item();
        item2.setTitle("Some game2");
        item2.setId(2L);
        item2.setRating(7.5);
        item2.setAdditionalInfo("false");
        item2.setCategory(savedCategory);

        Item item3 = new Item();
        item3.setTitle("game2");
        item3.setId(3L);
        item3.setRating(8.0);
        item3.setAdditionalInfo("false");
        item3.setCategory(savedCategory);

        when(itemRepository.findByCategoryIdAndRatingGreaterThanEqual(1L,7.5))
                .thenReturn(List.of(item2, item3));

        ItemContainerDto result = itemService.getItemsWithRatingGreaterThan(1L,7.5);

        assertNotNull(result);
        assertEquals(2,result.getAllItems().size());

        assertEquals("Some game2",result.getAllItems().get(0).getTitle());
        assertEquals("false",result.getAllItems().get(0).getAdditionalInfo());
        assertEquals(7.5,result.getAllItems().get(0).getRating());

        assertEquals("game2",result.getAllItems().get(1).getTitle());
        assertEquals("false",result.getAllItems().get(1).getAdditionalInfo());
        assertEquals(8.0,result.getAllItems().get(1).getRating());

        verify(itemRepository, times(1)).findByCategoryIdAndRatingGreaterThanEqual(1L,7.5);
    }

}
