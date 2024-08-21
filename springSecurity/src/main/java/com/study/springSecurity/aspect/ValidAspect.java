package com.study.springSecurity.aspect;

import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.exception.ValidException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Slf4j
@Aspect
@Component // IOC 등록
public class ValidAspect {

    @Pointcut("@annotation(com.study.springSecurity.aspect.annotation.ValidAop)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { // proceedingJoinPoint = signup 메소드
        Object[] args = proceedingJoinPoint.getArgs(); // Object 배열 = 업캐스팅해서 가져옴

        BeanPropertyBindingResult bindingResult = null; // BeanPropertyBindingResult = signup의 BindingResult

        for(Object arg : args) {
            if(arg.getClass() == BeanPropertyBindingResult.class) { // BeanPropertyBindingResult = 오류에 해당하는 오류 객체를 담음?
                bindingResult = (BeanPropertyBindingResult) arg; // arg는 Object로 들어가 있으니까 다운캐스팅 해줌
                // arg가 BeanPropertyBindingResult 여야만 다운캐스팅 가능
                break;
            }
        }

        for(Object arg : args) { // args가 ReqSignupDto 일때만 동작
            if(arg.getClass() == ReqSignupDto.class) {
                ReqSignupDto dto = (ReqSignupDto) arg;
                if(!dto.getPassword().equals(dto.getCheckPassword())) { // 두개가 서로 다르면
                    FieldError fieldError = new FieldError("checkPassword", "checkPassword", "비밀번호가 일치하지 않습니다.");
                    bindingResult.addError(fieldError);
                }
                break;
            }
        }

//        for(FieldError fieldError : bindingResult.getFieldErrors()) {// 에러 객체를 반복으로 꺼냄- 없으면 실행 안됨
//            System.out.println(fieldError.getField()); // 필드네임
//            System.out.println(fieldError.getDefaultMessage()); // message
//        }
        if(bindingResult.hasErrors()) { // bindingResult에 에러가 있는지 검사함
            throw new ValidException("유효성 검사 오류", bindingResult.getFieldErrors()); // 400에러로 떠야함 - ValidException으로 예외를 던짐
        } // 예외를 응답해주는 컨트롤러로 가져감. 원래의 컨트롤러로 가는게 아님 : 컨트롤러 어드바이스

        return proceedingJoinPoint.proceed();
    }
}
