package com.luga_online.controller;

import com.luga_online.model.AuthUser;
import com.luga_online.service.FriendUtils;
import com.luga_online.to.FriendTo;
import com.luga_online.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(InviteController.REST_URL)
@EnableAutoConfiguration
public class InviteController {
    static final String REST_URL = "/invite";

    private final FriendUtils friendUtils;

    @Autowired
    public InviteController(FriendUtils friendUtils) {
        this.friendUtils = friendUtils;
    }

    @GetMapping(value = "/getFriends", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FriendTo> user(@AuthenticationPrincipal AuthUser user) {
        return friendUtils.getFriendsForView(user);
    }

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
