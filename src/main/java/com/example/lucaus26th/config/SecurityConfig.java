package com.example.lucaus26th.config;

import com.example.lucaus26th.jwt.JwtAuthenticationFilter;
import com.example.lucaus26th.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationFilter jwtFilter;
    private final OAuthSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS
                .cors(SecurityConfig::corsAllow)

                // CSRF OFF
                .csrf(AbstractHttpConfigurer::disable)

                // 기본 로그인 기능 제거
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 인가 설정
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/",
//                                "/join",
//                                "/login",
//                                "/oauth2/**",
//                                "/login/oauth2/**",
//                                "/h2-console/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )

                // 일단 임시로 모든 api 허용
                .authorizeHttpRequests(auth -> auth
                        // 부스 좋아요는 로그인 필요
                        .requestMatchers(HttpMethod.POST, "/booth/*/like/").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/booth/*/like/").authenticated()
                        // 나머지는 전부 허용 (꼭 맨 마지막에 둘것)
                        .anyRequest().permitAll()
                )

                // 소셜 로그인
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2UserService)
                        )
                        // 여기서 JWT 발급 예정
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                // H2 console 접근 허용
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // JWT 필터
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private static void corsAllow(CorsConfigurer<HttpSecurity> corsConfigurer) {
        corsConfigurer.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);

            return configuration;
        });
    }
}