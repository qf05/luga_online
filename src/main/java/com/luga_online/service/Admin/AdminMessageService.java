package com.luga_online.service.Admin;

import com.luga_online.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminMessageService {

    private final MessageService messageService;

    public AdminMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
