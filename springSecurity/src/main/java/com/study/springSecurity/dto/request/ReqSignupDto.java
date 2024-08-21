package com.study.springSecurity.dto.request;

import com.study.springSecurity.domain.entity.User;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ReqSignupDto {
    @Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자 이름은 6자이상의 영문 소문자, 숫자 조합이어야 합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*?])[a-zA-Z\\d~!@#$%^&*?]{8,16}$"
            , message = "비밀번호는 8자이상, 16자이하의 영문 대소문자, 숫자, 특수문자(~!@#$%^&*?)중 하나 이상 포함하여야 합니다."
    )
    private String password;
    private String checkPassword;
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글로만 이루어져야 합니다.")
    private String name;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .build();
    }
}
