package com.luga_online.controller;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Pay;
import com.luga_online.service.PayService;
import com.luga_online.util.Utils;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
@RequestMapping(PayController.REST_URL)
public class PayController {
    static final String REST_URL = "/money";

    private final PayService payService;
    private static Set<AuthUser> progressPaySet = new CopyOnWriteArraySet<>();

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @GetMapping(value = "/moneyCount")
    public String moneyCount(@AuthenticationPrincipal AuthUser user) {
        return Utils.convertMoney(user.getUser().getMoney());
    }

    @GetMapping(value = "/phone")
    public String getPhone(@AuthenticationPrincipal AuthUser user) {
        return user.getUser().getTel();
    }

    @GetMapping(value = "/pay")
    public String pay(@AuthenticationPrincipal AuthUser user, String phone, Double money) {
        payService.setPhone(user.getUser(), phone);
        String result = "Error";
        if (!progressPaySet.contains(user)) {
            progressPaySet.add(user);
            result = payService.pay(user.getUser().getVkId(), money);
            progressPaySet.remove(user);
        }
        return result;
    }

    @GetMapping(value = "/historyPay", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pay> historyPay(@AuthenticationPrincipal AuthUser user) {
        return payService.getHistoryPay(user.getId());
    }
}
