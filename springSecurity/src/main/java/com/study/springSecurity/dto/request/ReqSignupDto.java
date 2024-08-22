package com.study.springSecurity.dto.request;

import com.study.springSecurity.domain.entity.Role;
import com.study.springSecurity.domain.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
public class ReqSignupDto {
    @Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자 이름은 6자이상의 영문 소문자, 숫자 조합이어야 합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*?])[a-zA-Z\\d~!@#$%^&*?]{8,16}$"
            , message = "비밀번호는 8자이상, 16자이하의 영문 대소문자, 숫자, 특수문자(~!@#$%^&*?)중 하나 이상 포함하여야 합니다.")
    private String password;
    private String checkPassword;
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글로만 이루어져야 합니다.")
    private String name;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) { // 매개변수로 암호화 하는 로직 받아옴 - 호출되는 곳이 서비스라서 Dto가 컴포넌트가 아님에도 IOC기능 사용 가능
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password)) // 비밀번호는 암호화 되어서 들어가야함
                .name(name)
                .build();
    }
}
