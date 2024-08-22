package com.study.springSecurity.aspect;

import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.exception.ValidException;
import com.study.springSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Slf4j
@Aspect
@Component // IOC 등록
public class ValidAspect {

    @Autowired
    private SignupService signupService;

    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.ValidAop)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { // proceedingJoinPoint = signup 메소드
        // 낚아채서 ProceedingJoinPoint에 signup메소드의 정보를 가지고 온다
        Object[] args = proceedingJoinPoint.getArgs(); // Object 배열 = 업캐스팅해서 가져옴

        BeanPropertyBindingResult bindingResult = null; // BeanPropertyBindingResult = signup의 BindingResult

        for(Object arg : args) {
            // 해당 arg의 클래스가 BeanPropertyBindingResult이거니?
            if(arg.getClass() == BeanPropertyBindingResult.class) { // BeanPropertyBindingResult = 오류에 해당하는 오류 객체를 담음?
                bindingResult = (BeanPropertyBindingResult) arg; // arg는 Object로 들어가 있으니까 다운캐스팅 해줌
                // arg가 BeanPropertyBindingResult 여야만 다운캐스팅 가능
                break; // 찾았으니까 뒤에 동작 실행될 필요 없음
            }
        }
        // 메소드가 동작하기 전과 후에 AOP를 달아주는 것/ 클래스는 동작을 담을 수 있는 객체/ 동작의 주체 = 메소드
        switch (proceedingJoinPoint.getSignature().getName()) { // proceedingJoinPoint = 핵심 로직(signup) 정보 getSignature = 정보들 가져올 수 있음 / getName = signup
            case "signup":
                validSignupDto(args, bindingResult);
                break;
        }

//        for(FieldError fieldError : bindingResult.getFieldErrors()) {// 에러 객체를 반복으로 꺼냄- 없으면 실행 안됨
//            System.out.println(fieldError.getField()); // 필드네임
//            System.out.println(fieldError.getDefaultMessage()); // message
//        }
        if(bindingResult.hasErrors()) { // bindingResult에 에러가 있는지 검사함
            throw new ValidException("유효성 검사 오류", bindingResult.getFieldErrors()); // 400에러로 떠야함 - ValidException으로 예외를 던짐
        } // 예외를 응답해주는 컨트롤러로 가져감. 원래의 컨트롤러로 가는게 아님 : 컨트롤러 어드바이스

        return proceedingJoinPoint.proceed(); // 이때 컨트롤러 안에 있는 메소드 signup을 실행함
        // proceed = 핵심기능(컨트롤러 메소드) 호출
        // 클라이언트 - 프록시 - 컨트롤러 - 어라운드에서 호출 - 컨트롤러에꺼 실행 - 어라운드로 반환 - 후처리가 있으면 그것도 동작 - 프록시 - 클라이언트
        // 후처리 만들려면 result 뭐시기로 함
        // around가 컨트롤러로 리턴 - 컨트롤러가 어라운드로 리턴 - 프록시로 리턴?????????
    }

    // 비밀번호, 아이디 체크
    private void validSignupDto(Object[] args, BeanPropertyBindingResult bindingResult) { // bindingResult객체의 주소를 보내니까 리턴이 필요없음
        for (Object arg : args) { // args에 ReqSignupDto가 있을때만 동작
            if (arg.getClass() == ReqSignupDto.class) {
                ReqSignupDto dto = (ReqSignupDto) arg;
                if (!dto.getPassword().equals(dto.getCheckPassword())) { // 두개가 서로 다르면
                    FieldError fieldError = new FieldError("checkPassword", "checkPassword", "비밀번호가 일치하지 않습니다.");
                    bindingResult.addError(fieldError);
                }
                if(signupService.isDuplicatedUsername(dto.getUsername())) { // true = 중복되었다
                    FieldError fieldError = new FieldError("username", "username", "이미 존재하는 사용자 이름 입니다.");
                    bindingResult.addError(fieldError);
                }
                break;
            }
        }
    }
}
