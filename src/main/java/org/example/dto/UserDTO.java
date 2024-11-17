package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;               // 사용자 고유 ID (기본키, 오토인크리먼트)
    private String email;          // 사용자 이메일
    private String username;       // 사용자 아이디
    private String password;       // 사용자 비밀번호
    private String role = "USER";  // 사용자 권한 (기본값: USER)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updateAt;
}