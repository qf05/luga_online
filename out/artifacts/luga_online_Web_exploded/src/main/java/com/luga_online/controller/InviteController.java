package com.luga_online.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@EnableAutoConfiguration
public class InviteController {

    @GetMapping("/invite")
    public String invitePage() {
        return "hello";
    }

    @PostMapping("/invite")
    public String invite() {
        return "hello";
    }


}
