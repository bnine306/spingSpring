package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CartDTO;
import org.example.dto.ProductDTO;
import org.example.repository.CartRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository; // 마이바티스를 통한 장바구니 레포지토리
    private final ProductRepository productRepository;

    public void addToCart(CartDTO cartDTO) {
        // 상품 정보 조회 (상품 id로 상품 설명 가져오기)
        ProductDTO product = productRepository.getProductById(cartDTO.getProductid());

        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }

        // 상품 설명 설정
        cartDTO.setProduct_description(product.getContent());  // 예: product.getDescription()에 상품 설명이 있다고 가정

        // 장바구니에 상품이 이미 존재하는지 체크
        if (cartRepository.existsByUserIdAndProductId(cartDTO.getUserid(), cartDTO.getProductid())) {
            // 이미 장바구니에 존재하면 수량 업데이트
            cartRepository.updateQuantity(cartDTO.getUserid(), cartDTO.getProductid(), cartDTO.getQuantity());
        } else {
            // 존재하지 않으면 새로 추가
            cartRepository.save(cartDTO);
        }
    }

    // 특정 사용자의 장바구니 항목 가져오기
    public List<CartDTO> getCartItems(Long userId) {
        return cartRepository.findCartItemsByUserId(userId); // CartDTO에 product_description이 포함됨
    }

    // 장바구니에서 상품 삭제
    public void removeFromCart(Long userId, Long productId) {
        // 사용자의 장바구니에서 특정 상품을 삭제하는 로직
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    // 사용자의 모든 장바구니 항목 삭제
    public void removeAllCartItems(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }

}