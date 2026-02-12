package com.mita.repository;

import com.mita.entity.Item;
import com.mita.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    List<Item> findByCategoryId(Long categoryId, Sort sort);

    Page<Item> findAll(Specification<Item> spec, Pageable pageable);

    Optional<Item> findByIdAndUser(Long id, User user);

}
