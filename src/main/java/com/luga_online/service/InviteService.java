package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Group;
import com.luga_online.model.Invite;
import com.luga_online.model.User;
import com.luga_online.repository.InviteRepository;
import com.luga_online.util.VkQueries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InviteService {

    private final InviteRepository inviteRepository;
    private final UserService userService;

    @Autowired
    public InviteService(InviteRepository inviteRepository, UserService userService) {
        this.inviteRepository = inviteRepository;
        this.userService = userService;
    }

    @Transactional
    public Map<Group, String> invite(AuthUser user, Integer friendId, List<Group> groups) {
        Map<Group, String> result = new HashMap<>();
        for (Group group : groups) {
            Object[] objects = VkQueries.inviteFriendToGroup(user, friendId, group.getGroupId());
            Integer resultCode = (Integer) objects[0];
            String message = (String) objects[1];
            result.put(group, message);
            if (resultCode == 1) {
                User userDB = user.getUser();
                userDB.setMoney(userDB.getMoney() + group.getPrice());
                userService.updateUser(userDB);
            }
            inviteRepository.save(new Invite(user.getUser(), group.getGroupId(), friendId, resultCode, Calendar.getInstance().getTimeInMillis()));
        }
        return result;
    }

    public Set<Integer> getInvitesToGroup(Integer userId, Integer groupID) {
        List<Invite> invites = inviteRepository.getAllByUserVkIdAndGroupId(userId, groupID);
        return invites.stream().map(Invite::getInvitedId).collect(Collectors.toSet());
    }

    public List<Invite> getUserInvites(Integer userId) {
        return inviteRepository.getAllByUserVkId(userId);
    }

//    @Autowired
//    private UserRepository userRepository;
//
//    private void ser(AuthUser user1){
//        User user = user1.getUser();
//        int id = user.getVkId();
//        Invite invite = new Invite(user, 111, 1345646);
//
//
//        int id1 = user1.getId();
//        User one = userRepository.getOne(id1);
//        Invite invite1 = new Invite(one, 111, 1345646);
//    }
}
