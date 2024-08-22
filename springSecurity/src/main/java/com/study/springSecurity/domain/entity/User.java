package com.study.springSecurity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity // 이걸 달면 테이블이 됨
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db에서 AI
    private Long id;
    @Column(unique = true, nullable = false) // 중복불가
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    // fetch : 엔티티를 조인했을 때 연관된 데이터를 언제 가져올지 결정(EAGER - 당장, LAZY - 나중에 사용할 때)
    // cascade : 외래키쓰면 써야함 /

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"), // user_id = id(프라이머리키를 자동으로 jpa가 만듦) - 조인 할 때 씀
            inverseJoinColumns = @JoinColumn(name = "role_id") // role_id = Role에 id - user_id에 있는 권한(role_id)를 다 가져옴
    )
    private Set<Role> roles; // roles = user_roles // 중복제거를 위해서 Set으로 받음

}
