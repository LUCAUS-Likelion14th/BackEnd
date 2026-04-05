package com.example.lucaus26th.controller;

import com.example.lucaus26th.service.MyPageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "마이페이지", description = "마이페이지에 사용할 API입니다.")
public class MyPageController {

    private final MyPageService myService;


}
