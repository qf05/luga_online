package com.luga_online.controller;

import com.luga_online.model.AuthUser;
import com.luga_online.to.UserTo;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(InviteController.REST_URL)
@EnableAutoConfiguration
public class InviteController {
    static final String REST_URL = "/invite";

    private final VkApiClient vk;

    @Autowired
    public InviteController(VkApiClient vk) {
        this.vk = vk;
    }

//    @GetMapping("/invite")
//    public String user(@AuthenticationPrincipal AuthUser user) {
//        int countFriend = 0;
//        try {
//            countFriend = vk.friends().get(user.getActor()).execute().getCount();
//        } catch (ApiException | ClientException e) {
//            e.printStackTrace();
//        }
//        System.out.println("count friends = " + countFriend);
//        return "invite";
//    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTo userInfo(@AuthenticationPrincipal AuthUser user) {
        double money = (double) user.getUser().getMoney() / 100;
        return new UserTo(user.getUserName(), user.getPhoto(), String.format("%.2f", money));
//        return new UserTo(user.getUserName(), null, String.format("%.2f", money));
    }

    @GetMapping(value = "/text")
    public String testUTF() {
        return "Русский текст";
    }

}
