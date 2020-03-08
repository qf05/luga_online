package com.luga_online.controller;

import com.luga_online.model.AuthUser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@EnableAutoConfiguration
public class InviteController {

    @Autowired
    private VkApiClient vk;

    @GetMapping("/invite")
    public String user(@AuthenticationPrincipal AuthUser user) {
        int countFriend = 0;
        try {
            countFriend = vk.friends().get(user.getActor()).execute().getCount();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        System.out.println("count friends = " + countFriend);
        return "count friends = " + countFriend;
    }


//    @PostMapping("/invite")
//    public String invite() {
//        return "hello";
//    }


}
