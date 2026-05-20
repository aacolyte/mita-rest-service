package com.mita.repository;

import com.mita.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findTop10ByUsernameContainingIgnoreCase(String username);


    boolean existsByUsername(String name);

    boolean existsByEmail(String email);
}
