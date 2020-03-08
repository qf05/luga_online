package com.luga_online.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@EnableAutoConfiguration
public class MainController {

    @GetMapping("/")
    public String main() {
        return "hello";
    }

    @PostMapping("/")
    public String mainPost(String uri) {
        System.out.println(uri);
        return "hello";
    }
}
