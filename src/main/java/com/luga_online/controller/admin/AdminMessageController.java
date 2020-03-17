package com.luga_online.controller.admin;

import com.luga_online.service.Admin.AdminMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AdminMessageController.REST_URL)
public class AdminMessageController {
    static final String REST_URL = "/admin/message";

    private final AdminMessageService service;

    public AdminMessageController(AdminMessageService service) {
        this.service = service;
    }
}
