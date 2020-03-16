package com.luga_online.controller;

import com.luga_online.model.AuthUser;
import com.luga_online.service.FriendsService;
import com.luga_online.service.GroupService;
import com.luga_online.service.InviteService;
import com.luga_online.to.FriendTo;
import com.luga_online.to.GroupToForInvite;
import com.luga_online.to.UserTo;
import com.luga_online.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(InviteController.REST_URL)
public class InviteController {
    static final String REST_URL = "/invite";

    private final FriendsService friendUtils;
    private final InviteService inviteService;

    @Autowired
    public InviteController(FriendsService friendUtils, GroupService groupService, InviteService inviteService) {
        this.friendUtils = friendUtils;
        this.inviteService = inviteService;
    }

    @GetMapping(value = "/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FriendTo> getFriends(@AuthenticationPrincipal AuthUser user) {
        return friendUtils.getFriendsForView(user);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTo userInfo(@AuthenticationPrincipal AuthUser user) {
        return new UserTo(user.getUserName(), user.getPhoto(), Utils.convertMoney(user.getUser().getMoney()));
//        return new UserTo(user.getUserName(), null, Utils.convertMoney(user.getUser().getMoney()));
    }

    @GetMapping(value = "/invite", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupToForInvite> invite(@AuthenticationPrincipal AuthUser user, Integer friendId, List<GroupToForInvite> groupsTo) {
        return inviteService.invite(user, friendId, groupsTo);
    }

    @GetMapping(value = "/text")
    public String testUTF() {
        return "Русский текст";
    }
}
