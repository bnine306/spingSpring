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
            @RequestParam String arrivaldate,
            @RequestParam String price,
            @RequestParam String category,
            @RequestParam("image") MultipartFile image,
            @RequestParam String content) { // 콘텐츠 필드 추가

        Map<String, String> response = new HashMap<>();
        try {
            LocalDate parsedArrivalDate = LocalDate.parse(arrivaldate);
            Long parsedPrice = Long.parseLong(price);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductnum(productnum);
            productDTO.setArrivaldate(parsedArrivalDate);
            productDTO.setPrice(parsedPrice);
            productDTO.setCategory(category);
            productDTO.setImage(image);
            productDTO.setContent(content); // 콘텐츠 추가

            String imageUrl = productService.addProduct(productDTO);

            if (imageUrl != null) {
                response.put("message", "상품 추가 성공");
                response.put("imageUrl", imageUrl);
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