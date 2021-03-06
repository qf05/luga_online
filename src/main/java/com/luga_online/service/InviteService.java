package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Group;
import com.luga_online.model.Invite;
import com.luga_online.model.User;
import com.luga_online.repository.InviteRepository;
import com.luga_online.to.GroupToForInvite;
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
    private final GroupService groupService;

    @Autowired
    public InviteService(InviteRepository inviteRepository, UserService userService, GroupService groupService) {
        this.inviteRepository = inviteRepository;
        this.userService = userService;
        this.groupService = groupService;
    }

    public List<Invite> getAllInvite() {
        return inviteRepository.findAll();
    }

    public void removeInvite(Integer inviteId) {
        inviteRepository.deleteById(inviteId);
    }

    public List<GroupToForInvite> invite(AuthUser user, Integer friendId, List<GroupToForInvite> groupsTo) {
        List<Integer> groupsId = groupsTo.stream().map(GroupToForInvite::getId).collect(Collectors.toList());
        Map<Integer, GroupToForInvite> groupsToIdMap = groupsTo.stream().collect(Collectors.toMap(GroupToForInvite::getId, i -> i));
        List<Group> groups = groupService.getGroupsById(groupsId);
        String message;
        for (Group group : groups) {
            if (group.isActive() && group.getLimitInvited() > 0) {
                Object[] objects = VkQueries.inviteFriendToGroup(user, friendId, group.getGroupId());
                Integer resultCode = (Integer) objects[0];
                message = (String) objects[1];
                saveResultInvite(resultCode, group, user, friendId);
            } else {
                message = "Limit invite to this group";
            }
            groupsToIdMap.get(group.getGroupId()).setMessage(message);
        }
        return new ArrayList<>(groupsToIdMap.values());
    }

    public Set<Integer> getInvitesToGroup(Integer userId, Integer groupID) {
        List<Invite> invites = inviteRepository.getAllByUserVkIdAndGroupId(userId, groupID);
        return invites.stream().map(Invite::getInvitedId).collect(Collectors.toSet());
    }

    public List<Invite> getUserInvites(Integer userId) {
        return inviteRepository.getAllByUserVkId(userId);
    }

    @Transactional
    void saveResultInvite(Integer resultCode, Group group, AuthUser user, Integer friendId) {
        if (resultCode == 1) {
            User userDB = user.getUser();
            userDB.setMoney(userDB.getMoney() + group.getPrice());
            userService.updateUser(userDB);
            user.setUser(userDB);
            group.setAllInvited(group.getAllInvited() + 1);
            group.setLimitInvited(group.getLimitInvited() - 1);
            if (group.getLimitInvited() < 1) {
                group.setActive(false);
            }
            groupService.saveGroup(group);
        }
        inviteRepository.save(new Invite(user.getUser(), group.getGroupId(), friendId, resultCode, Calendar.getInstance().getTimeInMillis()));

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
