package com.luga_online.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main() {
        return "hello";
    }

    @GetMapping("/invite")
    public String invite() {
        return "invite";
    }

    @GetMapping("/groups")
    public String groups() {
        return "groups";
    }


//    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public String mainPost(String uri) {
//        System.out.println(uri);
//        return "hello";
//    }
}
