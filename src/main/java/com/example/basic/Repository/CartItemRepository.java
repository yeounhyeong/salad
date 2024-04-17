package com.example.basic.Repository;

import com.example.basic.DTO.CartDTO;
import com.example.basic.DTO.CartItemDTO;
import com.example.basic.Entity.CartEntity;
import com.example.basic.Entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {

    //장바구니 상품 찾기
    @Query(value = "SELECT w FROM CartItemEntity w WHERE w.cartEntity.cartId = :cartId")
    CartItemEntity findByCartId(@Param("cartId") Integer cartId);

    //장바구니 상품 찾기
    @Query(value = "SELECT w FROM CartItemEntity w WHERE w.cartEntity.cartId = :cartId")
    List<CartItemEntity> findByCart(@Param("cartId") Integer cartId);

    CartItemEntity findByCartEntity_CartIdAndProductEntity_ProductId(Integer cartId, Integer productId);

    @Query(value = "SELECT w FROM CartItemEntity w WHERE w.cartEntity.cartId = :cartId")
    List<CartItemEntity> findAllByCartEntity(Integer cartId);
}
