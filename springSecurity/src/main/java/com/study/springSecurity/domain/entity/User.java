package com.study.springSecurity.domain.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

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


}
