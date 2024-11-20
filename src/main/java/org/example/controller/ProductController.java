package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProductDTO;
import org.example.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/productinput")
    public ResponseEntity<Map<String, String>> addProduct(
            @RequestParam String productnum,
            @RequestParam String arrivaldate,  // String으로 받아서 파싱
            @RequestParam String price,  // String으로 받음
            @RequestParam String category,
            @RequestParam("image") MultipartFile image) {  // image로 변경

        Map<String, String> response = new HashMap<>();
        try {
            // arrivaldate를 String에서 LocalDate로 변환
            LocalDate parsedArrivalDate = LocalDate.parse(arrivaldate);

            // price를 String에서 Long으로 변환
            Long parsedPrice = Long.parseLong(price);

            // productDTO를 새로 생성하고 각 필드를 세팅
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductnum(productnum);
            productDTO.setArrivaldate(parsedArrivalDate);  // 변환된 LocalDate 값 설정
            productDTO.setPrice(parsedPrice);  // 변환된 Long 값 설정
            productDTO.setCategory(category);
            productDTO.setImage(image);  // MultipartFile로 받은 이미지를 설정

            // 서비스 메소드 호출 (상품 추가)
            String imageUrl = productService.addProduct(productDTO);  // imageUrl 반환

            if (imageUrl != null) {
                response.put("message", "상품 추가 성공");
                response.put("imageUrl", imageUrl);  // 이미지 URL 반환
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "상품 추가 실패");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (IOException e) {
            response.put("message", "파일 처리 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (DateTimeParseException e) {
            response.put("message", "입고일 형식 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NumberFormatException e) {
            response.put("message", "가격 형식 오류");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/api/products")
    public List<ProductDTO> getProductsByCategoryAndSort(
            @RequestParam(required = false) String category,//해당 파라미터가 요청에 반드시 포함될 필요가 없다
            @RequestParam(required = false) String sortOrder) {

        return productService.getProductsByCategoryAndSort(category, sortOrder);
    }

    @GetMapping("/api/products/all")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/api/products/{numericProductId}")
    public ResponseEntity<ProductDTO> getProductDetail(@PathVariable Long numericProductId) {
        ProductDTO product = productService.getProductDetail(numericProductId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}