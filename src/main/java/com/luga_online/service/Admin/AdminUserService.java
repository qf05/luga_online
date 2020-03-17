package com.luga_online.service.Admin;

import com.luga_online.model.User;
import com.luga_online.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminUserService {

    private final UserService userService;

    public AdminUserService(UserService userService) {
        this.userService = userService;
    }

    public User getUser(Integer userId) {
        return userService.getUser(userId);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<User> getFilterUsers(Integer vkId,
                                     String tel,
                                     Long minMoney,
                                     Long maxMoney,
                                     Boolean allowMessages,
                                     Boolean banned,
                                     Integer excludeGroupId) {
        List<User> allUsers = userService.getAllUsers();
        Map<Integer, Set<User>> excludeMap = new HashMap<>();
        allUsers.forEach(i ->
                i.getExcludeGroups().forEach(g -> {
                    if (excludeMap.get(g.getId()) == null) {
                        excludeMap.put(g.getId(), Collections.singleton(i));
                    } else {
                        excludeMap.get(g.getId()).add(i);
                    }
                }));
        return allUsers.stream().filter(i ->
                (vkId == null || vkId.equals(i.getVkId()))
                        && (tel == null || tel.equals(i.getTel()))
                        && (minMoney == null || minMoney < i.getMoney())
                        && (maxMoney == null || maxMoney > i.getMoney())
                        && (allowMessages == null || allowMessages.equals(i.isAllowMessages()))
                        && (banned == null || banned.equals(i.isBanned()))
                        && (excludeGroupId == null || excludeMap.get(excludeGroupId).contains(i))
        ).collect(Collectors.toList());
    }

    public void updateUserMoney(Integer userId, Double moneyD) {
        long money = (long) (moneyD * 100);
        User user = userService.getUser(userId);
        if (user != null) {
            user.setMoney(money);
            userService.updateUser(user);
        }
    }

    public void changeBannedUser(Integer userId) {
        User user = userService.getUser(userId);
        user.setBanned(!user.isBanned());
        userService.updateUser(user);
    }

    public void removeUser(Integer userId) {
        userService.removeUser(userId);
    }


}
