package com.study.springSecurity.security.filter;

import com.study.springSecurity.domain.entity.User;
import com.study.springSecurity.repository.UserRepository;
import com.study.springSecurity.security.jwt.JwtProvider;
import com.study.springSecurity.security.principal.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String bearerAccessToken = request.getHeader("Authorization"); // 토큰 가져옴
        if(bearerAccessToken != null) {
            String accessToken = jwtProvider.removeBearer(bearerAccessToken); // 가져온 토큰에서 처리함
            Claims claims = null;
            try {
                claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }

            Long userId = ((Integer) claims.get("userId")).longValue(); // 롱을 인트로 바꾸고 그 인트를 롱으로 바꿔야함
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) { // 토큰은 존재하지만 계정은 삭제된 경우
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }

            PrincipalUser principalUser = optionalUser.get().toPrincipalUser();
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(),principalUser.getAuthorities());
            System.out.println("예외 발생하지 않음");
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증의 최종 목표
            // 이게 있어야 인증(로그인)이 되었다고 봄 - 필터를 유저네임뭐시기 필터 앞에 넣어놨음
            // 로그인 - 토큰 생성 - 유효한지 검사 - 인증
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
