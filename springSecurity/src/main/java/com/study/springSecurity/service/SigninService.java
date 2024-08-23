package com.study.springSecurity.service;

import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSigninDto;
import com.study.springSecurity.dto.response.RespJwtDto;
import com.study.springSecurity.repository.UserRepository;
import com.study.springSecurity.security.jwt.JwtProvider;
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

    @Autowired
    private JwtProvider jwtProvider;

    public RespJwtDto signin(ReqSigninDto dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요.")
        ); // Optional
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요.");
        }
        return RespJwtDto.builder()
                .accessToken(jwtProvider.generateUserToken(user)) // accessToken에 넣어서 리턴함
                .build(); // 토큰 만들어서 리턴함
    }
}
