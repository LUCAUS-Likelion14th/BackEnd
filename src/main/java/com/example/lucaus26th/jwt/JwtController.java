package com.example.lucaus26th.jwt;

import com.example.lucaus26th.dto.request.ReissueRequestDto;
import com.example.lucaus26th.dto.response.TokenResponseDto;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/reissue")
    public TokenResponseDto reissue(@RequestBody ReissueRequestDto request) {
        return jwtService.reissue(request.getRefreshToken());
    }
}