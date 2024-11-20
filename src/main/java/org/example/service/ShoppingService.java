package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.repository.ShoppingRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingService {

    private final ShoppingRepository shoppingRepository;

    // 모든 유저 조회
    public List<UserDTO> getUserList() {
        return shoppingRepository.findAll();
    }

    // 이메일 중복 체크
    public boolean isEmailDuplicate(String email) {
        UserDTO existingUser = shoppingRepository.findByEmail(email);
        return existingUser != null; // 이미 이메일이 존재하면 중복 처리
    }

    // 회원가입 처리
    public boolean registerUser(UserDTO userDTO) {
        // 현재 시간을 설정하여 createAt, updateAt에 명시적으로 값을 설정
        Date now = new Date();
        userDTO.setCreateAt(now);
        userDTO.setUpdateAt(now);

        // 이메일 중복 체크 후 저장
        if (isEmailDuplicate(userDTO.getEmail())) {
            return false; // 이메일 중복 시 회원가입 실패
        }

        // 회원가입 처리 (저장)
        return shoppingRepository.saveUser(userDTO);
    }


    // 로그인 시 비밀번호 체크
    public boolean loginCheck(String email, String password) {
        // 이메일로 사용자 정보를 조회
        UserDTO user = shoppingRepository.loginPass(email);

        // 사용자 정보가 존재하면 비밀번호 비교
        if (user != null) {
            // 비밀번호가 일치하는지 확인
            return user.getPassword().equals(password); // 암호화 없이 비밀번호 비교
        }

        // 이메일이 없으면 false 반환
        return false;
    }

    public UserDTO getUserByEmail(String email) {
        return shoppingRepository.findByEmail(email);
    }


    // 사용자 정보 조회
    public UserDTO getUserById(Long userId) {
        return shoppingRepository.findById(userId);
    }

    public UserDTO updateUser(Long userId, UserDTO updatedUserDTO) {
        UserDTO existingUser = shoppingRepository.findById(userId);

        if (existingUser != null) {
            // 수정할 필드만 업데이트
            existingUser.setEmail(updatedUserDTO.getEmail());
            existingUser.setUsername(updatedUserDTO.getUsername());
            existingUser.setPassword(updatedUserDTO.getPassword());
            existingUser.setUpdateAt(new Date());  // 수정일 업데이트

            // 저장된 정보 업데이트
            shoppingRepository.updateUser(existingUser);
            return existingUser;
        }
        return null;
    }

    public boolean deleteUser(Long userId) {
        // 사용자 삭제 처리
        return shoppingRepository.deleteUser(userId);
    }
}