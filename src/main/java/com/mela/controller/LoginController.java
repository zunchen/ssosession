package com.mela.controller;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("login")
    public String login(HttpServletRequest request, HttpSession session, String userName, String password) {

        if (!StringUtils.isEmpty((String) session.getAttribute("userName"))) {
            return "index";
        }

        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return "login";
        }

        session.setAttribute("userName", userName);
        session.setAttribute("port", request.getLocalPort());
        System.out.println(request.getLocalAddr());

        return "index";
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request, HttpSession session) {

        session.removeAttribute("userName");
        session.removeAttribute("port");
        System.out.println(request.getLocalAddr());

        return "login";
    }
}
