package com.luga_online.service;

import com.luga_online.model.Role;
import com.luga_online.model.User;
import com.luga_online.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository repository;

    public User findUser(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public User createUser(Integer id) {
        return repository.save(new User(id, Role.USER, null, 0, false));
    }
}
