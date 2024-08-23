package com.study.springSecurity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Role {
    @Id // 얘를 아이디로 씀
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 오토 인크리먼트
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;        //ROLE_USER, ROLE_ADMIN, ROLE_MANAGER
//
//    @ManyToMany(mappedBy = "roles") // 다대다 관계 / User에 들어있는 roles
//    private Set<User> users; //user들 마다 등록된 권한들이 있을 것

//    @OneToMany(mappedBy = "role") // mappedBy = UserRole(두개를 연결해서 새로 만들 테이블명) 에서 쓴 Entity의 변수명
//    private Set<UserRole> userRoles = new HashSet<>(); // HashSet 초기화 - nullpoint가 안뜨게 하기 위해서

}
