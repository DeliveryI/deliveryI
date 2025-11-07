package com.sparta.deliveryi.global.presentation.webapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping("/")
    public String redirectToSwagger() {
        // 루트("/") 요청 시 Swagger UI로 리다이렉트
        return "redirect:/swagger-ui/index.html";
    }
}
