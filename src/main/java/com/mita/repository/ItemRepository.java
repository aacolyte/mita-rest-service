package com.mita.repository;

import com.mita.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoryId(Long categoryId);

    List<Item> findByCategoryIdAndTitleContainingIgnoreCase(Long categoryId, String title);


    List<Item> findByCategoryIdAndRating(Long categoryId, Double rating);

    List<Item> findByCategoryIdAndRatingGreaterThanEqual(Long categoryId, Double rating);
}
