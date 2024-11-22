package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CartDTO;
import org.example.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;  // 서비스 의존성 주입

    // 장바구니에 상품 추가
    @PostMapping("/api/cart/add")
    public String addToCart(@RequestBody CartDTO cartDTO) {
        System.out.println(cartDTO);
        try {
            cartService.addToCart(cartDTO); // 서비스에서 장바구니에 추가하는 로직
            return "장바구니에 상품이 추가되었습니다.";
        } catch (Exception e) {
            return "장바구니에 추가하는데 실패했습니다: " + e.getMessage();
        }
    }

    // 특정 사용자의 장바구니 가져오기
    @GetMapping("/api/cart/{userId}")
    public List<CartDTO> getCartItems(@PathVariable Long userId) {
        return cartService.getCartItems(userId);
    }

    // 장바구니에서 상품 삭제
    @DeleteMapping("/api/cart/remove/{userId}/{productId}")
    public String removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            cartService.removeFromCart(userId, productId);  // 서비스에서 장바구니에서 삭제하는 로직
            return "장바구니에서 상품이 삭제되었습니다.";
        } catch (Exception e) {
            return "장바구니에서 상품을 삭제하는데 실패했습니다: " + e.getMessage();
        }
    }

    // 장바구니에서 모든 상품 삭제 (사용자 계정 삭제 시)
    @DeleteMapping("/api/cart/{userId}")
    public String removeAllCartItems(@PathVariable Long userId) {
        try {
            cartService.removeAllCartItems(userId);
            return "장바구니 항목이 모두 삭제되었습니다.";
        } catch (Exception e) {
            return "장바구니 항목 삭제에 실패했습니다.";
        }
    }

}