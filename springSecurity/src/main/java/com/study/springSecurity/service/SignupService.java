package com.study.springSecurity.service;

import com.study.springSecurity.aspect.annotation.TimeAop;
import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    @TimeAop // db에 저장되는게 몇초인지
    public User signup(ReqSignupDto dto) {
        return userRepository.save(dto.toEntity());
    }
}
