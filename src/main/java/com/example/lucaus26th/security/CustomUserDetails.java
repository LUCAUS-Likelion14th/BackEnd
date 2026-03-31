package com.example.lucaus26th.security;

import com.example.lucaus26th.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public Long getId(){
        return member.getId();
    }
    @Override
    public String getUsername(){
        return member.getName();
    }
    public String getEmail(){
        return member.getEmail();
    }
    public Integer getStudentId(){
        return member.getStudentID();
    }
    @Override
    public String getPassword() {
        return null; // OAuth 로그인이면 null
    }

    @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(); // role 추가되면 여기서 반환
        }

    // 아래는 전부 true 고정
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
