package com.study.springSecurity.controller;

import com.study.springSecurity.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> get() {

        System.out.println(testService.aopTest());
        testService.aopTest2("김준일", 31);
        testService.aopTest3("000-0000-0000", "부산시");
        return ResponseEntity.ok("확인");
    }
}

/*
*   400에러 - 백엔드의 기능 외의 기능을 요청한 오류 - 클라이언트 오류
*   params - get요청때만 사용
*   post - body에 JSON으로 보낼거다?
*       컨트롤러에서 서비스로 가서 AopTest를 호출 - 저게 바로 실행되는게 아니라 Aspect로 가서 proceed가 호출되면 저게 실행됨(service에 그게 aspect에 result에 들어감
* */