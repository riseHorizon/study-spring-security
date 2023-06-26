package com.horizon.controller;

import com.horizon.dto.UserBean;
import com.horizon.service.AuthService;
import com.horizon.util.MyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/common/")
public class LoginController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public UserBean login(UserBean loginUser, HttpServletRequest request) {
        UserBean user = authService.userLogin(loginUser);
        if (null != user) {
            log.info("user login succeed");
            request.getSession().setAttribute(MyConstants.FLAG_CURRENTUSER, user);
        }
        log.info("user login failed");
        return user;
    }

    @PostMapping("/getCurrentUser")
    public Object getCurrentUser(HttpSession session) {
        return session.getAttribute(MyConstants.FLAG_CURRENTUSER);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.removeAttribute(MyConstants.FLAG_CURRENTUSER);
    }
}
