package com.study.springSecurity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ReqSigninDto {
    @NotBlank(message = "사용자 이름을 입력하세요.")
    private String username;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}
