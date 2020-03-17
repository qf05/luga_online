package com.luga_online.controller.admin;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Invite;
import com.luga_online.model.Role;
import com.luga_online.service.Admin.AdminInviteService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AdminInviteController.REST_URL)
public class AdminInviteController {
    static final String REST_URL = "/admin/invite";

    private final AdminInviteService service;

    public AdminInviteController(AdminInviteService service) {
        this.service = service;
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Invite> getPays(@AuthenticationPrincipal AuthUser user) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getAllInvite();
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Invite> filterPays(@AuthenticationPrincipal AuthUser user,
                                   Integer vkId,
                                   Integer groupId,
                                   Integer invitedId,
                                   Integer result,
                                   Integer excludeResult,
                                   String startTime,
                                   String endTime) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getFilterInvite(vkId, groupId, invitedId, result, excludeResult, startTime, endTime);
    }

    @GetMapping(value = "/remove")
    public void removeInvite(@AuthenticationPrincipal AuthUser user, Integer inviteId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.removeInvite(inviteId);
    }
}
