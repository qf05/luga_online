package com.luga_online.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableAutoConfiguration
public class MainController {

    @GetMapping("/")
    public String main() {
        return "hello";
    }

    @GetMapping("/invite")
    public String invite() {
        return "invite";
    }


//    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public String mainPost(String uri) {
//        System.out.println(uri);
//        return "hello";
//    }
}
