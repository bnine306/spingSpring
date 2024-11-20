package org.example.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.dto.ProductDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final SqlSessionTemplate sql;

    public boolean saveProduct(ProductDTO productDTO){
        int result = sql.insert("Product.saveProduct", productDTO);
        return result > 0;
    }

    public List<ProductDTO> getProducts(String category) {
        return sql.selectList("Product.getProducts", category);
    }


    public List<ProductDTO> getAllProducts() {
        return sql.selectList("Product.getAllProducts");
    }

    // 카테고리별로 가격 낮은 순으로 정렬된 상품 가져오기
    public List<ProductDTO> getProductsByCategoryOrderByPriceAsc(String category) {
        return sql.selectList("Product.getProductsByCategoryOrderByPriceAsc", category);
    }

    // 카테고리별로 가격 높은 순으로 정렬된 상품 가져오기
    public List<ProductDTO> getProductsByCategoryOrderByPriceDesc(String category) {
        return sql.selectList("Product.getProductsByCategoryOrderByPriceDesc", category);
    }

    // 가격 낮은 순으로 모든 상품 가져오기
    public List<ProductDTO> getAllProductsOrderByPriceAsc() {
        return sql.selectList("Product.getAllProductsOrderByPriceAsc");
    }

    // 가격 높은 순으로 모든 상품 가져오기
    public List<ProductDTO> getAllProductsOrderByPriceDesc() {
        return sql.selectList("Product.getAllProductsOrderByPriceDesc");
    }

    // 상품 ID로 상품 조회
    public ProductDTO getProductById(Long productId) {
        return sql.selectOne("Product.getProductById", productId);  // Long 타입으로 받아서 처리
    }
}
