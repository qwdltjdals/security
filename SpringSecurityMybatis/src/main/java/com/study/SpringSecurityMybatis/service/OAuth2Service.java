package com.study.SpringSecurityMybatis.service;

import com.study.SpringSecurityMybatis.dto.request.ReqOAuth2MergeDto;
import com.study.SpringSecurityMybatis.repository.OAuth2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class OAuth2Service implements OAuth2UserService {



    @Autowired
    private DefaultOAuth2UserService defaultOAuth2UserService;

    @Autowired
    private OAuth2Mapper oAuth2Mapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // userRequest - 로그인한 유저의 정보들이 담겨있음
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

//        System.out.println(userRequest.getClientRegistration());
//        System.out.println(oAuth2User.getAttributes());
//        System.out.println(oAuth2User.getAuthorities());
//        System.out.println(oAuth2User.getName());

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> oAuth2Attributes = new HashMap<>();
        oAuth2Attributes.put("provider", userRequest.getClientRegistration().getClientName()); // 클라이언트 이름 = naver, kakao등

        switch (userRequest.getClientRegistration().getClientName()) {
            case "Google":
                oAuth2Attributes.put("id", attributes.get("sub").toString());
                break;
            case "Naver":
                attributes = (Map<String, Object>) attributes.get("response"); // 실제 응답 = response
                oAuth2Attributes.put("id", attributes.get("id").toString());
                break;
            case "Kakao":
        }
        return new DefaultOAuth2User(new HashSet<>(), oAuth2Attributes, "id"); // 키, 값
    }

    public void merge(com.study.SpringSecurityMybatis.entity.OAuth2User oAuth2User) {
        oAuth2Mapper.save(oAuth2User);
    }
}
