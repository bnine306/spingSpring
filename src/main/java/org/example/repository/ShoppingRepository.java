package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShoppingRepository {
    private final SqlSessionTemplate sql;

    // 모든 사용자 조회
    public List<UserDTO> findAll() {
        return sql.selectList("User.FindAll");
    }

    // 이메일로 사용자 조회
    public UserDTO findByEmail(String email) {
        return sql.selectOne("User.FindByEmail", email);
    }

    // 사용자 정보 저장
    public boolean saveUser(UserDTO userDTO) {
        int result = sql.insert("User.saveUser", userDTO);
        return result > 0; // 저장 성공 시 true 반환
    }


    // 로그인 시 이메일로 사용자 정보 조회
    public UserDTO loginPass(String email) {//DTO 형식으로 받아야 이메일, 비밀번호 비교가능
        return sql.selectOne("User.LoginPass", email); // 매퍼에서 정의된 쿼리 실행
    }
}