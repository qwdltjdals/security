package com.study.springSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect     // aspect 만들때 클래스명만 같고 나머지는 같은 형태
@Order(value = 2)
public class TestAspect { // aspect는 위에 두 @ 달아줌

    // execution = 광범위하게 쓸때 사용함 - 단일 대상 아님
    @Pointcut("execution(* com.study.springSecurity.service.TestService.aop*(..))") // .. = 매개변수가 0개 이상
    private void pointCut() {} // 비어있는 메소드 만듦 - 어느 지점을 잘라서 아래 기능을 넣을거다

    @Around("pointCut()") // 함수 호출 해당 포인트컷 위치에 아래거를 추가함
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("전처리");
        Object result = proceedingJoinPoint.proceed(); // proceed 빨간줄 누르면 throws Throwable 추가됨 - 핵심기능 호출 - 리턴 X
        // 원래 리턴값이 없으면 대입 불가. 하지만 aspect 라이브러리라서 가능
        System.out.println("후처리");
        // 순서 = 전처리 - 후처리 - 테스트입니다.

        return result; // 이때 리턴함
    }
}