package com.luga_online.controller;

import com.google.gson.Gson;
import com.luga_online.model.AuthUser;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class InviteController {

    @GetMapping("/invite")
    public String invitePage(){
        return "hello";
    }

    @PostMapping("/invite")
    public String invite(){
        return "hello";
    }


}
