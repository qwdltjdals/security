package com.study.springSecurity.service;

import com.study.springSecurity.aspect.annotation.TimeAop;
import com.study.springSecurity.domain.entity.Role;
import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.repository.RoleRepository;
import com.study.springSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @TimeAop // db에 저장되는게 몇초인지
    public User signup(ReqSignupDto dto) {
        User user = dto.toEntity(passwordEncoder);
        Role role = roleRepository.findByName("ROLE_USER").orElseGet( // db에 ROLE_USER 없으면 하나 만들고
                () -> roleRepository.save(Role.builder().name("ROLE_NAME").build()) // 거기에 이름 추가해서 가져와라
                );
        user.setRoles(Set.of(role));
        return userRepository.save(user); // 이때 비밀번호 암호화 된걸 매개변수로 넘겨줌
    }

    public boolean isDuplicatedUsername(String username) {//중복인지 아닌지 체크
        return userRepository.findByUsername(username).isPresent(); // isPresent = null인지 체크
    }
}
