package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.dto.CartDTO;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final SqlSessionTemplate sql;

    // 장바구니에 상품이 존재하는지 확인 (상품ID, 사용자ID로 체크)
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        // 파라미터 전달
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);

        // 반환값을 Integer로 캐스팅 후 비교
        Integer count = sql.selectOne("Cart.existsByUserIdAndProductId", params);

        return count != null && count > 0;  // count가 null이 아닌지 확인 후 0보다 큰지 비교
    }

    // 장바구니에 상품 추가
    public void save(CartDTO cartDTO) {
        sql.insert("Cart.save", cartDTO);
    }

    // 장바구니에 수량 업데이트
    public void updateQuantity(Long userId, Long productId, int quantity) {
        sql.update("Cart.updateQuantity", new CartDTO(userId, productId, quantity));
    }

    // 특정 사용자의 장바구니 항목 가져오기
    public List<CartDTO> findCartItemsByUserId(Long userId) {
        return sql.selectList("Cart.findCartItemsByUserId", userId); // 사용자 ID로 장바구니 항목 조회
    }

    // 장바구니에서 상품 삭제
    public void deleteByUserIdAndProductId(Long userId, Long productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("productId", productId);

        sql.delete("Cart.deleteByUserIdAndProductId", params);  // 매퍼에서 해당 쿼리를 실행
    }

    // 사용자의 모든 장바구니 항목 삭제
    public void deleteAllByUserId(Long userId) {
        sql.delete("Cart.deleteAllByUserId", userId);
    }
}