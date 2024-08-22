package com.study.springSecurity.controller;

import com.study.springSecurity.aspect.annotation.ParamsAop;
import com.study.springSecurity.aspect.annotation.ValidAop;
import com.study.springSecurity.dto.request.ReqSigninDto;
import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.service.SigninService;
import com.study.springSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController { // 인증 관련 요청들이 여기 들어옴

    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;

    @ParamsAop
    @PostMapping("/signup")
    @ValidAop // 원래 signup이라는 메소드를 호출하려 했는데, 얘가 낚아채버림
    // bindingResult에는 에러가 들어감 / dto에는 저장에 성공한 값만 들어감 - Valid가 핕터를 함
    public ResponseEntity<?> signup(@Valid @RequestBody ReqSignupDto dto,
                                        BindingResult bindingResult) { // Valid가 달려있으면 자동으로 정규식 체크를 함(Patton)
        return ResponseEntity.created(null).body(signupService.signup(dto));
    }

    @ValidAop
    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @Valid @RequestBody ReqSigninDto dto,
            BeanPropertyBindingResult bindingResult) {
        signinService.signin(dto);
        return ResponseEntity.ok().body(null);
    }
}
// transaction : 과정 중에서 하나의 단계라도 실패하면 전부 롤백