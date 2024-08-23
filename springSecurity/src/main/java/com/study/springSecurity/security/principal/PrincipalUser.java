package com.study.springSecurity.security.principal;

import com.study.springSecurity.domain.entity.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class PrincipalUser implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // Authorities 권한들이 들어감 - role
        //<? extends GrantedAuthority> = GrantedAuthority 이 객체를 상속받은 녀석만 올수있음
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        for(Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return authorities;  - 밑에꺼랑 같은 코드
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }


    @Override
    public boolean isAccountNonExpired() { // 계정 만료
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠김
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 인증이 만료
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
