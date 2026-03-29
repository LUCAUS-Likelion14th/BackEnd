package com.example.lucaus26th.controller;


import com.example.lucaus26th.dto.request.BoothRequestDto;
import com.example.lucaus26th.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booth/")
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

    @PostMapping
    public ResponseEntity<Long> createBooth(@RequestBody  BoothRequestDto request){
        Long boothId = boothService.createBooth(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(boothId);
    }
}
