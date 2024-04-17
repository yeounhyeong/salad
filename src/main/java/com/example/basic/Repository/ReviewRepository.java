package com.example.basic.Repository;

import com.example.basic.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    //조회수 증가
    @Modifying
    @Query("UPDATE ReviewEntity u SET u.reviewCount= u.reviewCount+1" +
            " WHERE u.reviewId=:reviewId")
    void reviewCount(Integer reviewId);
}
