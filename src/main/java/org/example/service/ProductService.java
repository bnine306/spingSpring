package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProductDTO;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private static final String IMAGE_FOLDER = "C:/Users/USER/Desktop/spingbackupfile/homereactCategory/public/images"; // 이미지 저장 폴더

    // 상품 추가 및 이미지 저장
    public String addProduct(ProductDTO productDTO) throws IOException {
        // 이미지 저장 경로 설정 (예: 현재 시간 기반으로 파일 이름 지정)
        String imageUrl = saveImage(productDTO.getImage());  // image로 변경

        // 이미지 경로에서 /public을 제거
        if (imageUrl != null && imageUrl.startsWith("/public")) {
            imageUrl = imageUrl.replace("/public", ""); // /public을 제거
        }

        productDTO.setImageUrl(imageUrl);  // 이미지 URL을 String 필드에 저장

        // 상품 정보를 DB에 저장하는 로직
        boolean isSaved = productRepository.saveProduct(productDTO);

        if (isSaved) {
            return imageUrl;  // 성공적으로 저장되었을 경우 이미지 URL 반환
        } else {
            return null;  // 실패 시 null 반환
        }
    }

    // 이미지 저장
    public String saveImage(MultipartFile image) throws IOException {
        // 이미지 파일이 비어있지 않으면 저장
        if (image != null && !image.isEmpty()) {
            // 현재 시간을 기반으로 파일 이름 생성
            String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date()); // 년월일만 출력
            String imageName = timeStamp + "_" + image.getOriginalFilename();
            String imagePath = IMAGE_FOLDER + File.separator + imageName;

            // 파일 저장
            File destinationFile = new File(imagePath);
            image.transferTo(destinationFile);  // 파일을 실제로 저장

            // 저장된 이미지의 경로 반환 (웹에서 접근 가능한 URL로 변환)
            return "/images/" + imageName; // 상대 경로 반환
        } else {
            return null; // 이미지가 없으면 null 반환
        }
    }

    public List<ProductDTO> getProductsByCategoryAndSort(String category, String sortOrder) {
        if (category != null && !category.isEmpty()) {
            // 카테고리 필터링과 정렬
            if ("lowToHigh".equals(sortOrder)) {
                return productRepository.getProductsByCategoryOrderByPriceAsc(category);
            } else if ("highToLow".equals(sortOrder)) {
                return productRepository.getProductsByCategoryOrderByPriceDesc(category);
            }
            return productRepository.getProducts(category);  // 기본 카테고리 필터링
        }

        // 카테고리 없는 경우, 정렬만 적용
        if ("lowToHigh".equals(sortOrder)) {
            return productRepository.getAllProductsOrderByPriceAsc();
        } else if ("highToLow".equals(sortOrder)) {
            return productRepository.getAllProductsOrderByPriceDesc();
        }

        return productRepository.getAllProducts();  // 카테고리도 정렬도 없는 경우
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public ProductDTO getProductDetail(Long numericProductId) {
        // repository에서 제품을 조회
        return productRepository.getProductById(numericProductId);
    }
}