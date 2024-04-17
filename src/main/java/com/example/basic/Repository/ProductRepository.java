package com.example.basic.Repository;

import com.example.basic.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends
        JpaRepository<ProductEntity, Integer> {
//    //상품명 조회
//    List<ProductEntity> findByProductNameContaining(String keyword);
//
//    //상품 리스트 조회
//    List<ProductEntity> findByProductId(Integer productId);
//
//    //신상품 조회
//    @Query("SELECT w from ProductEntity w order by w.regDate desc")
//    List<ProductEntity> findProductEntitiesBy();

}
