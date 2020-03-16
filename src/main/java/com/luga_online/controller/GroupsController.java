package com.luga_online.controller;

import com.luga_online.model.AuthUser;
import com.luga_online.service.GroupService;
import com.luga_online.service.MessageService;
import com.luga_online.to.GroupTo;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(GroupsController.REST_URL)
public class GroupsController {
    static final String REST_URL = "/groups";

    private final GroupService groupService;

    private final MessageService messageService;

    public GroupsController(GroupService groupService, MessageService messageService) {
        this.groupService = groupService;
        this.messageService = messageService;
    }

    @GetMapping(value = "/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupTo> getGroups(@AuthenticationPrincipal AuthUser user) {
        return groupService.getGroupsTo(user);
    }

    @GetMapping(value = "/exclude", produces = MediaType.APPLICATION_JSON_VALUE)
    public void excludeGroup(@AuthenticationPrincipal AuthUser user, Integer groupId, boolean exclude) {
        groupService.excludeGroup(user.getUser(), groupId, exclude);
    }

    @GetMapping(value = "/allowMessages", produces = MediaType.APPLICATION_JSON_VALUE)
    public void allowMessages(@AuthenticationPrincipal AuthUser user) {
        messageService.changeAllowMessages(user);
    }

    @GetMapping(value = "/isAllowMessages", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isaAllowMessages(@AuthenticationPrincipal AuthUser user) {
        return user.getUser().isAllowMessages();
    }
}
