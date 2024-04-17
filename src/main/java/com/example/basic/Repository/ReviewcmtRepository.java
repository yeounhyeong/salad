package com.example.basic.Repository;

import com.example.basic.Entity.ReviewcmtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewcmtRepository extends JpaRepository<ReviewcmtEntity, Integer> {
    //@Query(value = "SELECT u FROM ReviewcmtEntity u WHERE u.reviewId=:reviewId", nativeQuery = true)
    @Query("SELECT s FROM ReviewcmtEntity s WHERE s.reviewEntity.reviewId = :reviewId")
    List<ReviewcmtEntity> findByReviewId(@Param("reviewId") Integer reviewId);
}
