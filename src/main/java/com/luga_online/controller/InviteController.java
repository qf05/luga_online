package com.luga_online.controller;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Group;
import com.luga_online.service.FriendUtils;
import com.luga_online.service.GroupService;
import com.luga_online.service.InviteService;
import com.luga_online.to.FriendTo;
import com.luga_online.to.UserTo;
import com.luga_online.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(InviteController.REST_URL)
@EnableAutoConfiguration
public class InviteController {
    static final String REST_URL = "/invite";

    private final FriendUtils friendUtils;
    private final GroupService groupService;
    private final InviteService inviteService;

    @Autowired
    public InviteController(FriendUtils friendUtils, GroupService groupService, InviteService inviteService) {
        this.friendUtils = friendUtils;
        this.groupService = groupService;
        this.inviteService = inviteService;
    }

    @GetMapping(value = "/getFriends", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FriendTo> user(@AuthenticationPrincipal AuthUser user) {
        return friendUtils.getFriendsForView(user);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTo userInfo(@AuthenticationPrincipal AuthUser user) {
        return new UserTo(user.getUserName(), user.getPhoto(), Utils.convertMoney(user.getUser().getMoney()));
//        return new UserTo(user.getUserName(), null, Utils.convertMoney(user.getUser().getMoney()));
    }

    @GetMapping(value = "/invite", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<Integer, String> invite(@AuthenticationPrincipal AuthUser user, Integer friendId, List<Integer> groupsId) {
        List<Group> groups = groupService.getGroupsById(groupsId);
        return inviteService.invite(user, friendId, groups);
    }


    @GetMapping(value = "/text")
    public String testUTF() {
        return "Русский текст";
    }
}
