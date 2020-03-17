package com.luga_online.controller.admin;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Role;
import com.luga_online.model.User;
import com.luga_online.service.Admin.AdminUserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AdminUserController.REST_URL)
public class AdminUserController {
    static final String REST_URL = "/admin/user";

    private final AdminUserService service;

    public AdminUserController(AdminUserService service) {
        this.service = service;
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@AuthenticationPrincipal AuthUser user, Integer userId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getUser(userId);
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers(@AuthenticationPrincipal AuthUser user) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getAllUsers();
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> filterUsers(@AuthenticationPrincipal AuthUser user,
                                  Integer vkId,
                                  String tel,
                                  Long minMoney,
                                  Long maxMoney,
                                  Boolean allowMessages,
                                  Boolean banned,
                                  Integer excludeGroupId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getFilterUsers(vkId, tel, minMoney, maxMoney, allowMessages, banned, excludeGroupId);
    }

    @GetMapping(value = "/money")
    public void updateUserMoney(@AuthenticationPrincipal AuthUser user, Integer userId, Double money) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.updateUserMoney(userId, money);
    }

    @GetMapping(value = "/ban")
    public void changeBannedUser(@AuthenticationPrincipal AuthUser user, Integer userId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.changeBannedUser(userId);
    }

    @GetMapping(value = "/remove")
    public void removeUser(@AuthenticationPrincipal AuthUser user, Integer userId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.removeUser(userId);
    }
}
