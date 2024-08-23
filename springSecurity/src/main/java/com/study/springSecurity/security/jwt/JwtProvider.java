package com.study.springSecurity.security.jwt;

import com.study.springSecurity.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;

    // Value = Autowired?
    public JwtProvider(@Value("${jwt.secret}") String secret) { // Value 에 표현식 쓰면 application에 있는 값 가져옴 String 문자열로
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // 암호화 해서 키를 넣어야함
    }

    public String removeBearer(String token) {
        return token.substring("Bearer ".length()); // Bearer 뒤 문자열의 크기만큼 잘라줌
    }

    // 세션 = 사용자 정보를 서버가 가지고있음 -
    // 토큰 = 사용자 정보를 클라이언트가 가지고있음 - 서버에서 요청을 할때 사용되는 유저 아이디가 들어있음(민감정보X)
    public String generateUserToken(User user) {

        Date expireDate = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 30)); // 현재시간 + 1000(1초) - 한달 유지
        String token = Jwts.builder()
                .claim("userId", user.getId()) // claim = 키벨류 추가할때 씀
                .expiration(expireDate) // 유효기간 30일
                .signWith(key, SignatureAlgorithm.HS256) // 해당 키로 인증해야 한다
                .compact();

        return token;
    }

    public Claims parseToken(String token) {
        JwtParser jwtParser = Jwts.parser() //암호화한거 다시 복호화
                .setSigningKey(key) // 키값 - 이 키로 서명을 하겠다.
                .build(); // 여기서 위에꺼까지 parser 객체를 생성함

        return jwtParser.parseClaimsJws(token).getPayload(); // 토큰안에 들어있는 Payload를 꺼냄 안에 claims가 들어있음
    }
}
