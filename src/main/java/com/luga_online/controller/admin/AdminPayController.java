package com.luga_online.controller.admin;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Pay;
import com.luga_online.model.Role;
import com.luga_online.service.Admin.AdminPayService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AdminPayController.REST_URL)
public class AdminPayController {
    static final String REST_URL = "/admin/pay";

    private final AdminPayService service;

    public AdminPayController(AdminPayService service) {
        this.service = service;
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pay> getPays(@AuthenticationPrincipal AuthUser user) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getAllPays();
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pay> filterPays(@AuthenticationPrincipal AuthUser user,
                                Integer vkId,
                                Long minMoney,
                                Long maxMoney,
                                String result,
                                String excludeResult,
                                String startTime,
                                String endTime) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return null;
        }
        return service.getFilterPays(vkId, minMoney, maxMoney, result, excludeResult, startTime, endTime);
    }

    @GetMapping(value = "/remove")
    public void removePay(@AuthenticationPrincipal AuthUser user, Integer payId) {
        if (!Role.ADMIN.equals(user.getUser().getRole())) {
            return;
        }
        service.removePay(payId);
    }
}
