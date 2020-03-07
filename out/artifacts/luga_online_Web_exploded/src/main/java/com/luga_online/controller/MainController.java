package com.luga_online.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.luga_online.model.AuthUser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@EnableAutoConfiguration
public class MainController {

    @Autowired
    private VkApiClient vk;

    @GetMapping("/")
    public String main(){
        return "hello";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal AuthUser user) {
        int countfriend = 0;
        try {
            countfriend = vk.friends().get(user.getActor()).execute().getCount();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        System.out.println("count friends = " + countfriend);
        return "count friends = " + countfriend;
    }

//    @PostMapping("/")
//    public String mainPost(){
//        return "hello";
//    }

    @GetMapping("/group")
    public String main(String uri){
        System.out.println(uri);
        return "group";
    }

    @PostMapping("/")
    public String mainPost(String uri){
        System.out.println(uri);
        return "hello";
    }
}
