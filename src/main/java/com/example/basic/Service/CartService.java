package com.example.basic.Service;

import com.example.basic.DTO.CartDTO;
import com.example.basic.DTO.CartItemDTO;
import com.example.basic.DTO.ProductDTO;
import com.example.basic.Entity.*;
import com.example.basic.Repository.CartItemRepository;
import com.example.basic.Repository.CartRepository;
import com.example.basic.Repository.MemberRepository;
import com.example.basic.Repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    //삽입
    @Transactional
    public void cartInsert(Integer memberId, ProductDTO productDTO, Integer quantity) {

        //회원 조회
        Optional<MemberEntity> member = memberRepository.findById(memberId);
        MemberEntity memberEntity = member.orElseThrow();

        //상품 조회
        Optional<ProductEntity> product = productRepository.findById(productDTO.getProductId());
        ProductEntity productEntity = product.orElseThrow();

        //회원의 장바구니 생성
        CartEntity cart = cartRepository.findByMemberId(memberEntity.getMemberId());
        if (cart == null) {
            cart = CartEntity.builder()
                    .memberEntity(memberEntity)
                    .build();
            cartRepository.save(cart);
        }

        //장바구니 상품 테이블 생성
        CartItemEntity cartItemEntity = cartItemRepository.findByCartEntity_CartIdAndProductEntity_ProductId(cart.getCartId(), productEntity.getProductId());

        //장바구니에 상품이 존재하지 않는다면 카트상품 추가
        if (cartItemEntity == null) {
            cartItemEntity = CartItemEntity.builder()
                    .cartEntity(cart)
                    .productEntity(productEntity)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(cartItemEntity);
        } else {
            CartItemEntity cartItem = cartItemEntity;
            //장바구니에 상품이 존재한다면 상품 수량 증가
            cartItem.setQuantity(quantity+1);
            cartItemRepository.save(cartItem);
        }
    }

    //수정
    @Transactional
    public void cartUpdate(CartItemDTO cartItemDTO, Integer memberId) {

        //회원 조회
        Optional<MemberEntity> member = memberRepository.findById(memberId);
        MemberEntity memberEntity = member.orElseThrow();

        //회원의 장바구니 조회
        CartEntity cart = cartRepository.findByMemberId(memberEntity.getMemberId());

        //장바구니 상품 조회
        CartItemEntity cartItemEntity = cartItemRepository.findByCartId(
                cart.getCartId());

        //장바구니에 상품이 존재한다면 상품 수량 변경
        cartItemEntity.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItemEntity);
    }

    //삭제
    @Transactional
    public void cartDelete(Integer cartItemId) {

        cartItemRepository.deleteById(cartItemId);
    }

    //전체 조회(장바구니 상품)
    @Transactional
    public List<CartItemDTO> cartList(Integer memberId) {
        //회원 정보를 찾아서 카트를 읽어온다
        CartEntity cart = cartRepository.findByMemberId(memberId);

        //장바구니가 존재하지 않는다면
        if (cart == null) {
            cart = CartEntity.builder()
                    .memberEntity(MemberEntity.builder()
                            .memberId(memberId)
                            .build())
                    .build();
            //장바구니 생성
            cartRepository.save(cart);
        }

        //장바구니 상품 찾기
        List<CartItemEntity> cartEntities = cartItemRepository.findAllByCartEntity(cart.getCartId());

        //장바구니에 상품이 존재하면
        if (cartEntities != null) {
            //상품 정보 담기
            List<CartItemDTO> cartItemDTOS = new ArrayList<>();
            for (CartItemEntity cartItemEntity : cartEntities) {
                //상품 리스트에 상품이 존재하면
                if (cartItemEntity != null) {
                    CartItemDTO cartItemDTO = modelMapper.map(cartItemEntity, CartItemDTO.class);
                    cartItemDTOS.add(cartItemDTO);
                }
            }
            return cartItemDTOS;
        } else {
            //장바구니에 상품이 존재하지 않으면
            return Collections.emptyList();
        }
    }

    //장바구니 아이템 조회(개별 조회)
    public CartItemDTO cartItemDetail(Integer cartItemId) {
        Optional<CartItemEntity> cartItemEntity = cartItemRepository.findById(cartItemId);
        CartItemDTO cartItemDTO = modelMapper.map(cartItemEntity, CartItemDTO.class);

        return cartItemDTO;
    }

    //회원의 장바구니 조회(개별조회)
    public CartDTO cartDetail(Integer memberId) {
        //회원 조회
        Optional<MemberEntity> member = memberRepository.findById(memberId);
        member.orElseThrow();
        //회원의 장바구니 생성
        CartEntity cart = cartRepository.findByMemberId(member.get().getMemberId());

        if (cart == null) {
            cart = CartEntity.builder()
                    .memberEntity(MemberEntity.builder()
                            .memberId(member.get().getMemberId())
                            .build())
                    .build();
            cartRepository.save(cart);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        return cartDTO;
    }

    //수량 증가(수정에서 처리)
    public void cartItemUpdate(Integer cartItemId, Integer quantity) {
        CartItemEntity cartItemEntity = cartItemRepository.findById(cartItemId).
                orElseThrow(EntityNotFoundException::new);
        cartItemEntity.setQuantity(quantity);
    }
}
