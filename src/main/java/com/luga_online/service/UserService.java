package com.luga_online.service;

import com.luga_online.model.Role;
import com.luga_online.model.User;
import com.luga_online.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return repository.save(new User(id, Role.USER, null, 0, false, false, null));
    }

    public void updateUser(User user) {
        repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUser(Integer userId) {
        if (userId == null) {
            return null;
        }
        return repository.findById(userId).orElse(null);
    }

    public void removeUser(Integer userId) {
        repository.deleteById(userId);
    }
}
