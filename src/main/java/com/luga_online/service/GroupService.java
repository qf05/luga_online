package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.ExcludeGroup;
import com.luga_online.model.Group;
import com.luga_online.model.User;
import com.luga_online.repository.ExcludeGroupRepository;
import com.luga_online.repository.GroupRepository;
import com.luga_online.to.GroupTo;
import com.luga_online.util.Utils;
import com.luga_online.util.VkQueries;
import com.vk.api.sdk.objects.groups.GroupFull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository repository;
    private final ExcludeGroupRepository excludeGroupRepository;

    @Autowired
    public GroupService(GroupRepository repository, ExcludeGroupRepository excludeGroupRepository) {
        this.repository = repository;
        this.excludeGroupRepository = excludeGroupRepository;
    }

    public List<Group> getAllGroups() {
        return repository.findAll();
    }

    public List<Group> getGroupsById(List<Integer> groupsId) {
        return repository.findAllById(groupsId);
    }

    public Group getGroupById(Integer groupId) {
        return repository.findById(groupId).orElse(null);
    }

    public void saveGroup(Group group) {
        repository.save(group);
    }

    public void removeGroup(Integer groupId) {
        repository.deleteById(groupId);
    }

    public List<Group> getFilterGroups(User user) {
        Set<Integer> excludeGroupsId = user.getExcludeGroups().stream()
                .map(ExcludeGroup::getExcludeGroupId)
                .collect(Collectors.toSet());
        return getAllGroups().stream()
                .filter(Group::isActive)
                .filter(i -> i.getLimitInvited() > 0)
                .filter(i -> !excludeGroupsId.contains(i.getGroupId()))
                .collect(Collectors.toList());
    }

    public List<GroupTo> getGroupsTo(AuthUser user) {
        List<Group> groups = getAllGroups().stream()
                .filter(i -> i.isActive() && i.getLimitInvited() > 0)
                .collect(Collectors.toList());

        Map<Integer, Group> groupsIdMap = groups.stream()
                .collect(Collectors.toMap(Group::getGroupId, i -> i));

        Set<Integer> excludeGroups = user.getUser().getExcludeGroups().stream()
                .map(ExcludeGroup::getExcludeGroupId)
                .collect(Collectors.toSet());

        List<String> groupsId = groupsIdMap.keySet().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        List<GroupFull> groupFulls = VkQueries.getGroups(user, groupsId);
        return groupFulls.stream().map(i -> {
            Integer id = Integer.parseInt(i.getId());
            return new GroupTo(id,
                    i.getName(),
                    i.getPhoto50(),
                    Utils.convertMoney(groupsIdMap.get(id).getPrice()),
                    !excludeGroups.contains(id));
        }).collect(Collectors.toList());
    }

    public void excludeGroup(User user, Integer groupId, boolean exclude) {
        if (exclude) {
            excludeGroupRepository.save(new ExcludeGroup(user, groupId));
        } else {
            ExcludeGroup excludeGroup = excludeGroupRepository.getFirstByExcludeGroupIdAndUser_VkId(groupId, user.getVkId());
            excludeGroupRepository.delete(excludeGroup);
        }
    }
}
