package com.mita.repository;

import com.mita.dto.PublicUserDto;
import com.mita.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {


    boolean existsByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    void deleteByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    long countByFollowing_Id(Long followingId);

    long countByFollower_Id(Long followerId);

    @Query("""
        SELECT new com.mita.dto.PublicUserDto(
            u.username,
            u.avatar,
            u.about,
            0,
            0
        )
        FROM Follow f
        JOIN f.follower u
        WHERE f.following.id = :userId
""")
    Page<PublicUserDto> findFollowers(Long userId, Pageable pageable);


    @Query("""
        SELECT new com.mita.dto.PublicUserDto(
            u.username,
            u.avatar,
            u.about,
            0,
            0
        )
        FROM Follow f
        JOIN f.following u
        WHERE f.follower.id = :userId
""")
    Page<PublicUserDto> findFollowings(Long userId, Pageable pageable);
}
