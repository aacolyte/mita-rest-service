package com.mita.repository;

import com.mita.entity.Item;
import com.mita.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    List<Item> findByCategoryId(Long categoryId, Sort sort);

    Page<Item> findAll(Specification<Item> spec, Pageable pageable);

    Optional<Item> findByIdAndUser(Long id, User user);


    @Query(value = """
    SELECT * FROM (
        SELECT i.*,
               ROW_NUMBER() OVER (
                   PARTITION BY category_id
                   ORDER BY rating DESC
               ) as rn
        FROM items i
        WHERE i.user_id = :userId
    ) t
    WHERE t.rn = 1
""", nativeQuery = true)
    List<Item> findTopItemsPerCategory(Long userId);

    int countByUserId(Long userId);

    @Query("""
        SELECT i.poster
        FROM Item i
        WHERE i.category.id = :categoryId
        AND i.poster IS NOT NULL
    """)
    Page<String> findPosterNameByCategoryId(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

}
