package com.luga_online.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableAutoConfiguration
public class YandexController {

    @GetMapping("/ya")
    public String payMeTest() {


        return "hello";
    }
}
