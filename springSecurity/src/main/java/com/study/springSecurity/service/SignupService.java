package com.study.springSecurity.service;

import com.study.springSecurity.aspect.annotation.TimeAop;
import com.study.springSecurity.domain.entity.Role;
import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.dto.request.ReqSignupDto;
import com.study.springSecurity.repository.RoleRepository;
import com.study.springSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor // AutoWired대신에 final달고 이거 달아줘도됨
public class SignupService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @TimeAop // db에 저장되는게 몇초인지
    @Transactional(rollbackFor = Exception.class) // rollbackFor 예외를 써줄 수 있음 - Exception 이 메소드 실행하다가 중간에 롤백 해라
    public User signup(ReqSignupDto dto) {
        User user = dto.toEntity(passwordEncoder);
        Role role = roleRepository.findByName("ROLE_USER").orElseGet( // db에 ROLE_USER 없으면 하나 만들고
                () -> roleRepository.save(Role.builder().name("ROLE_NAME").build()) // 거기에 이름 추가해서 가져와라
                );
        user.setRoles(Set.of(role)); // 이게 있어야 세이브 할때 같이 세이브 됨
        user = userRepository.save(user); // 이때 비밀번호 암호화 된걸 매개변수로 넘겨줌 - 유저를 먼저 save - user Id값 가져옴

//        UserRole userRole = UserRole.builder() // userRole이라는 테이블에 entity로 넣음
//                .user(user)
//                .role(role)
//                .build();
//        userRole = userRoleRepository.save(userRole);
//        user.setUserRoles(Set.of(userRole)); // 위에 리턴타입이 User 니까 userRole을 user에 넣어줌
        return user; // user 리턴
    }

    public boolean isDuplicatedUsername(String username) {//중복인지 아닌지 체크
        return userRepository.findByUsername(username).isPresent(); // isPresent = null인지 체크
    }
}
