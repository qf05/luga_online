package com.luga_online.service;

import com.luga_online.model.ExcludeGroup;
import com.luga_online.model.Group;
import com.luga_online.model.User;
import com.luga_online.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository repository;

    @Autowired
    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public List<Group> getAllGroups() {
        return repository.findAll();
    }

    public List<Group> getGroupsById(List<Integer> groupsId) {
        return repository.findAllById(groupsId);
    }

    public void updateGroup(Group group) {
        repository.save(group);
    }

    public List<Group> getFilterGroups(User user) {
        List<Group> groups = getAllGroups();
        List<Integer> excludeGroupsId = user.getExcludeGroups()
                .stream()
                .map(ExcludeGroup::getExcludeGroupId)
                .collect(Collectors.toList());
        return groups.stream()
                .filter(Group::isActive)
                .filter(i -> i.getLimitInvited() > 0)
                .filter(i -> !excludeGroupsId.contains(i.getGroupId()))
                .collect(Collectors.toList());
    }
}
