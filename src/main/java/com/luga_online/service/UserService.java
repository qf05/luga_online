package com.luga_online.service;

import com.luga_online.model.Role;
import com.luga_online.model.User;
import com.luga_online.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findUser(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public User createUser(Integer id) {
        return repository.save(new User(id, Role.USER, null, 0, false, null));
    }
}
