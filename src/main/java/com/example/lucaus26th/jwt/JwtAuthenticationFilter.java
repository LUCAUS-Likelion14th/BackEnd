package com.example.lucaus26th.jwt;


import com.example.lucaus26th.config.MemberAuthentication;
import com.example.lucaus26th.domain.Member;
import com.example.lucaus26th.enums.JwtValidationType;
import com.example.lucaus26th.repository.foodTruck.MemberRepository;
import com.example.lucaus26th.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.lucaus26th.enums.JwtValidationType.VALID_JWT;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = getJwtFromRequest(request);
            JwtValidationType jwtValidationType = jwtTokenProvider.validateToken(token);
            if (jwtValidationType == VALID_JWT) {
                Long memberId = jwtTokenProvider.getMemberIdFromAccessToken(token);
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
                CustomUserDetails userDetails = new CustomUserDetails(member);

                MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(userDetails); // ← 이걸로 변경
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.error("현재 상태: " + jwtValidationType.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}
