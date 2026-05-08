package com.example.lucaus26th.jwt;

import com.example.lucaus26th.enums.JwtValidationType;
import com.example.lucaus26th.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.lucaus26th.enums.JwtValidationType.VALID_JWT;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponseDto reissue(String refreshToken) {

        JwtValidationType type = jwtTokenProvider.validateToken(refreshToken);

        if (type != VALID_JWT) {
            throw new IllegalArgumentException("유효하지 않은 refresh token");
        }

        Long memberId =
                jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken);

        String newAccessToken =
                jwtTokenProvider.generateAccessToken(memberId);

        String newRefreshToken =
                jwtTokenProvider.generateRefreshToken(memberId);

        return TokenResponseDto.of(newAccessToken, newRefreshToken);
    }
}
