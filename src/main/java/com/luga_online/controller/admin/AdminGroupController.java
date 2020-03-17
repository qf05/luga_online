package com.luga_online.controller.admin;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Group;
import com.luga_online.model.Role;
import com.luga_online.service.Admin.AdminGroupService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AdminGroupController.REST_URL)
public class AdminGroupController {
    static final String REST_URL = "/admin/group";

    private final AdminGroupService service;

    public AdminGroupController(AdminGroupService service) {
        this.service = service;
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Group> getGroups(@AuthenticationPrincipal AuthUser user) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getAllGroups();
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public Group getGroup(@AuthenticationPrincipal AuthUser user, Integer groupId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getGroup(groupId);
    }

    @GetMapping(value = "/remove")
    public void removeGroup(@AuthenticationPrincipal AuthUser user, Integer groupId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.removeGroup(groupId);
    }

    @GetMapping(value = "/remove")
    public void updateGroup(@AuthenticationPrincipal AuthUser user,
                            Integer groupId,
                            Long price,
                            Integer limitInvited,
                            Integer allInvited,
                            Boolean active,
                            String cities,
                            Integer sex,
                            Integer minAge,
                            Integer maxAge) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.updateGroup(groupId, price, limitInvited, allInvited, active, cities, sex, minAge, maxAge);
    }

    @GetMapping(value = "/create")
    public void createGroup(@AuthenticationPrincipal AuthUser user,
                            Integer groupId,
                            Long price,
                            Integer limitInvited,
                            Integer allInvited,
                            Boolean active,
                            String cities,
                            Integer sex,
                            Integer minAge,
                            Integer maxAge) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.createGroup(groupId, price, limitInvited, allInvited, active, cities, sex, minAge, maxAge);
    }
}
