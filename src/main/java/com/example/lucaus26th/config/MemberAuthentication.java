package com.example.lucaus26th.config;

import com.example.lucaus26th.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {
    public MemberAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication createMemberAuthentication(Long memberId) {
        return new MemberAuthentication(memberId, null, null);
    }
    public static MemberAuthentication createMemberAuthentication(CustomUserDetails userDetails) {
        return new MemberAuthentication(userDetails, null, userDetails.getAuthorities());
    }
}
