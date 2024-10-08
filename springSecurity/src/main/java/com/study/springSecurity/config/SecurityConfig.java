package com.study.springSecurity.config;

import com.study.springSecurity.security.filter.JwtAccessTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity      // 우리가 만든 securityConfig를 적용시키겠다.
@Configuration // Ioc 컨테이너에 Bean으로 들어감 Bean등록 가능
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 추상클래스 상속 시킴 - 추상메소드가 있냐 없냐?

    @Autowired
    private JwtAccessTokenFilter jwtAccessTokenFilter;

    @Bean // Bean 만듦 - 컴포넌트 등록
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable();
        http.httpBasic().disable();
//        http.sessionManagement().disable();
        // 스프링 시큐리티가 세션을 생성하지도 않고 기존의 세션을 사용하지도 않겠다
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 스프링 시큐리티가 세션을 생성하지 않겠다. 기존의 세션을 완전히 사용하지 않겠다는 뜻은 아님
        // JWT 등의 토큰 인증 방식을 사용할 때 설정하는 것
        http.cors(); // crossorign - 프론트의 요청을 받아올 수 있도록 준비
        http.csrf().disable();
        // 위조방지 스티커(토큰)
        http.authorizeRequests()
                .antMatchers("/auth/**", "/h2-console/**") // antMatchers = 주소를 선택할 수 있음
                .permitAll() // 위에있는 모든것들을 인가 없이 사용해라
                .anyRequest() // 다른 모든 요청들
                .authenticated() // 인가를 거쳐라
                .and()
                .headers()
                .frameOptions()
                .disable();
        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 유저네임 뭐시기 필터 실행되기 이전에 저 필터 실행
    }
}

// protected = 같은 패키지 내에서 참조 가능 / 상속 받으면 접근 가능