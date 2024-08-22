package com.study.springSecurity.service;

import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSigninDto;
import com.study.springSecurity.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SigninService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void signin(ReqSigninDto dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요.")
        ); // Optional
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요.");
        }
        System.out.println("로그인 성공");
    }
}
