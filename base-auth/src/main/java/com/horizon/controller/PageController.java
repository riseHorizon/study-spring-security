package com.horizon.controller;

import com.horizon.util.CnAuthConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/baseAuth/page")
public class PageController {

    @GetMapping("/login")
    public String login(HttpSession session) {
        session.removeAttribute(CnAuthConstants.FLAG_CURRENTUSER);
        return "login";
    }

    @GetMapping("/auth/index")
    public String index() {
        return "index";
    }

}
