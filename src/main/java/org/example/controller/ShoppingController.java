package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.service.ShoppingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();

        // 이메일과 비밀번호를 함께 검사
        boolean isValidLogin = shoppingService.loginCheck(userDTO.getEmail(), userDTO.getPassword());

        if (!isValidLogin) {
            // 이메일이나 비밀번호가 일치하지 않으면 오류 메시지 반환
            response.put("message", "이메일 또는 비밀번호가 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
        }

        // 로그인 성공
        response.put("message", "로그인 성공");
        return ResponseEntity.ok(response); // 200 OK
    }
}