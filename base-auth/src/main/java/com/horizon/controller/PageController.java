package com.horizon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/baseAuth/page")
public class PageController {

    @GetMapping("/login")
    public String login() {
        int a = 1/0;
        return "login";
    }

    @GetMapping("/auth/index")
    public String index() {
        return "index";
    }

}
