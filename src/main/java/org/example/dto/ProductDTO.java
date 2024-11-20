package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ProductDTO {
    private Long productid;
    private String productnum;
    private Long price;
    private String category;
    private MultipartFile image;  // MultipartFile 타입을 image로 이름 변경
    private String imageUrl;  // 이미지 경로를 저장할 String 타입

    @JsonFormat(pattern = "yyyy-MM-dd")  // LocalDate 형식에 맞게 수정
    private LocalDate arrivaldate;  // LocalDate 타입으로 수정


}
