package com.sergeyrozhkov.spring.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String helloPage() {
        return "hello";
    }

    @GetMapping("/authenticated")
    public String authenticatedPart() {
        return "authenticated part of web application";
    }
}
