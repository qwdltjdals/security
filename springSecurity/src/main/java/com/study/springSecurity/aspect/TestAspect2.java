package com.study.springSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Order(value = 1)
@Component
@Aspect     // aspect 만들때 클래스명만 같고 나머지는 같은 형태

public class TestAspect2 { // aspect는 위에 두 @ 달아줌

    // execution = 광범위하게 쓸때 사용함 - 단일 대상 아님
    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.Test2Aop)") // .. = 매개변수가 0개 이상
    private void pointCut() {} // 비어있는 메소드 만듦 - 어느 지점을 잘라서 아래 기능을 넣을거다

    @Around("pointCut()") // 함수 호출 해당 포인트컷 위치에 아래거를 추가함
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature(); // 다운캐스팅 해줘야함 getSignature =Signature객체 반환
        System.out.println(signature.getName()); // 메소드명
        System.out.println(signature.getDeclaringTypeName()); // 메소드가 있는 class명

        Object[] args = proceedingJoinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        for(int i = 0; i < args.length; i++) {
            System.out.println(paramNames[i] + ":" + args[i]);
        }

        System.out.println("전처리2");
        Object result = proceedingJoinPoint.proceed(); // TestAspect 를 result에 넣음
        System.out.println("후처리2");
        // 순서 = 전처리 - 후처리 - 테스트입니다.

        return result; // 이때 리턴함
    }
}