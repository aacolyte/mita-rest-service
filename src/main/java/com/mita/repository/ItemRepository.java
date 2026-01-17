package com.mita.repository;

import com.mita.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoryId(Long categoryId);

    List<Item> findByCategoryIdAndTitleContainingIgnoreCase(Long categoryId, String title);


    List<Item> findByCategoryIdAndRating(Long categoryId, Double rating);

    List<Item> findByCategoryIdAndRatingGreaterThanEqual(Long categoryId, Double rating);

    @Query(
            value = "SELECT * FROM items WHERE category_id = :categoryId ORDER BY rating DESC LIMIT :limit",
            nativeQuery = true
    )
    List<Item> findTopItemsByRating(@Param("categoryId") Long categoryId, @Param("limit") Integer limit);

    List<Item> findByCategoryIdAndAdditionalInfoContainingIgnoreCase(Long categoryId, String genre);

    List<Item> findByCategoryIdAndAdditionalInfo(Long categoryId, String died);


}
