package com.sergeyrozhkov.spring.security.controllers;

import com.sergeyrozhkov.spring.security.entities.User;
import com.sergeyrozhkov.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MainController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String helloPage() {
        return "hello";
    }

    @GetMapping("/authenticated")
    public String authenticatedPart(Principal principal) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication(); // a содержит данные о принципал, креденшилах, авторитис из секьюрити контекста
        User user = userService.findByUsername(principal.getName());
        return "authenticated part of web application " + principal.getName() + " " + user.getEmail() ; // получаем имя залогиненного пользователя
    }

    @GetMapping("/read_profile")
    public String pageForReadProfile(Principal principal) {
        return "read profile with username:" + principal.getName();
    }

    @GetMapping("/only_for_admins")
    public String pageOnlyForAdmins() {
        return "admin's page ";
    }
}
