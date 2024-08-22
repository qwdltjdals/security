package com.study.springSecurity.init;

import com.study.springSecurity.domain.entity.Role;
import com.study.springSecurity.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception { // String... args = 갯수 상관 없이 받을 수 있고, 들어갈때 배열로들어감
        // role테이블에 ROLE_USER라는 이름이 없으면 TRUE
        if(roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }
        if(roleRepository.findByName("ROLE_MANAGER").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_MANAGER").build());
        }
        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        }
    }
}
