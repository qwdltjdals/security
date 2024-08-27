package com.study.SpringSecurityMybatis.service;

import com.study.SpringSecurityMybatis.dto.request.ReqSignupDto;
import com.study.SpringSecurityMybatis.dto.response.RespSignupDto;
import com.study.SpringSecurityMybatis.entity.Role;
import com.study.SpringSecurityMybatis.entity.User;
import com.study.SpringSecurityMybatis.entity.UserRoles;
import com.study.SpringSecurityMybatis.repository.RoleMapper;
import com.study.SpringSecurityMybatis.repository.UserMapper;
import com.study.SpringSecurityMybatis.repository.UserRolesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Boolean isDuplicateUsername(String username) {
        return Optional.ofNullable(userMapper.findByUsername(username)).isPresent(); // isPresent
    }

    public RespSignupDto insertUserAndUserRoles(ReqSignupDto dto) { // dto를 User로 바꿔줘야함
        User user = dto.toEntity(passwordEncoder);
        userMapper.save(user); // save함

        Role role = roleMapper.findByName("ROLE_USER"); // 있으면 들고옴
        if (role == null) {
            role = Role.builder().name("ROLE_USER").build(); // 없으면 만듦
            roleMapper.save(role);
        }

        UserRoles userRoles = UserRoles.builder() // 두개 조인하는거?
                .userId(user.getId())
                .roleId(role.getId())
                .build();

        userRolesMapper.save(userRoles);

        user.setUserRoles(Set.of(userRoles));

        return RespSignupDto.builder()
                .message("회원가입 완료")
                .user(user)
                .build();
    }
}
