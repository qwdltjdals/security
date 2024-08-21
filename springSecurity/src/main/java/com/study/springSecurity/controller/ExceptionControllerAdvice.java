package com.study.springSecurity.controller;

import com.study.springSecurity.exception.ValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 컴포넌트기 때문에 IOC안에서 터진 예외만 잡아낼 수 있음
public class ExceptionControllerAdvice {

    @ExceptionHandler(ValidException.class) // ValidException을 지켜보고 있음 - 예외가 터지나 안터지나 확인
    public ResponseEntity<?> validException(ValidException validException) { // 생성된 예외를 매개변수로 가져옴
        return ResponseEntity.badRequest().body(validException.getFieldErrors()); // ValidException 에서 Getter로 설정한거 가져옴
    }
}




// 회원가입 - dto에 값 가져감 - 유효성 검사 진행 - 에러가 남 - throw new ValidException 예외 터뜨림 - 여기서 대신 응답해줌