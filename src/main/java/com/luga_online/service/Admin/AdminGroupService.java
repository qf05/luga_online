package com.luga_online.service.Admin;

import com.luga_online.model.Group;
import com.luga_online.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminGroupService {

    private final GroupService groupService;

    public AdminGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    public Group getGroup(Integer groupId) {
        return groupService.getGroupById(groupId);
    }

    public void removeGroup(Integer groupId) {
        groupService.removeGroup(groupId);
    }

    public void updateGroup(Integer groupId,
                            Long price,
                            Integer limitInvited,
                            Integer allInvited,
                            Boolean active,
                            String cities,
                            Integer sex,
                            Integer minAge,
                            Integer maxAge) {
        Group group = groupService.getGroupById(groupId);
        if (group != null) {
            if (price != null) {
                group.setPrice(price);
            }
            if (limitInvited != null) {
                group.setLimitInvited(limitInvited);
            }
            if (allInvited != null) {
                group.setAllInvited(allInvited);
            }
            if (active != null) {
                group.setActive(active);
            }
            group.setCities(cities);
            group.setSex(sex);
            group.setMinAge(minAge);
            group.setMaxAge(maxAge);
            groupService.saveGroup(group);
        }
    }

    public void createGroup(Integer groupId,
                            Long price,
                            Integer limitInvited,
                            Integer allInvited,
                            Boolean active,
                            String cities,
                            Integer sex,
                            Integer minAge,
                            Integer maxAge) {
        Group group = new Group(groupId, price, limitInvited, allInvited, active, cities, sex, minAge, maxAge);
        groupService.saveGroup(group);
    }
}
