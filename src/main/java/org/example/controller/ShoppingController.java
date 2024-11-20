package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProductDTO;
import org.example.dto.UserDTO;
import org.example.service.ShoppingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShoppingController {

    private final ShoppingService shoppingService;

    // 모든 유저 조회
    @GetMapping("/api/users")
    public List<UserDTO> getUserList() {
        return shoppingService.getUserList();
    }

    // 회원가입 처리
    @PostMapping("/api/signup")
    public Map<String, String> signUp(@RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();

        // 이메일 중복 체크
        boolean isEmailDuplicate = shoppingService.isEmailDuplicate(userDTO.getEmail());

        if (isEmailDuplicate) {
            response.put("message", "이메일이 중복되었습니다.");
            return response; // JSON 형식으로 반환
        }

        // 이메일 중복이 아니라면 회원가입 처리
        boolean isSignedUp = shoppingService.registerUser(userDTO);

        if (isSignedUp) {
            response.put("message", "회원가입 성공");
        } else {
            response.put("message", "서버 오류");
        }

        return response; // JSON 형식으로 반환
    }

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();

        // 이메일과 비밀번호를 함께 검사
        boolean isValidLogin = shoppingService.loginCheck(userDTO.getEmail(), userDTO.getPassword());

        if (!isValidLogin) {
            // 이메일이나 비밀번호가 일치하지 않으면 오류 메시지 반환
            response.put("message", "이메일 또는 비밀번호가 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
        }

        // 로그인 성공 시 사용자 정보와 함께 메시지 반환
        UserDTO user = shoppingService.getUserByEmail(userDTO.getEmail());
        response.put("message", "로그인 성공");
        response.put("id", user.getId()); // id 값 추가
        return ResponseEntity.ok(response); // 200 OK
    }


    // 사용자 정보 조회 (GET 요청)
    @GetMapping("/api/user/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO user = shoppingService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/api/user/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = shoppingService.updateUser(userId, userDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


    }

    @DeleteMapping("/api/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        boolean isDeleted = shoppingService.deleteUser(userId);  // userId를 서비스에 전달
        if (isDeleted) {
            return ResponseEntity.ok("사용자 계정이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 삭제에 실패했습니다.");
        }
    }




}