package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.User;
import com.luga_online.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageService {

    private final UserRepository userRepository;

    public MessageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO
    public void sendMessage(String message) {

    }

    public void changeAllowMessages(AuthUser user) {
        if (user.getUser().isAllowMessages()) {
            unsubscribe(user);
        } else {
            subscribe(user);
        }
    }

    //TODO
    private void subscribe(AuthUser authUser) {
        User user = authUser.getUser();
        user.setAllowMessages(true);
        userRepository.save(user);
//        notifications.sendMessage
    }

    //TODO
    private void unsubscribe(AuthUser authUser) {
        User user = authUser.getUser();
        user.setAllowMessages(false);
        userRepository.save(user);
    }
}
