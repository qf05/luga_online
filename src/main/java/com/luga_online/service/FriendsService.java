package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Group;
import com.luga_online.model.Invite;
import com.luga_online.to.FriendTo;
import com.luga_online.util.Utils;
import com.luga_online.util.VkQueries;
import com.vk.api.sdk.objects.groups.MemberStatus;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.luga_online.util.Utils.getUserVkName;

@Service
@Slf4j
public class FriendsService {

    private final GroupService groupService;

    private final InviteService inviteService;

    @Autowired
    public FriendsService(GroupService groupService, InviteService inviteService) {
        this.groupService = groupService;
        this.inviteService = inviteService;
    }

    public List<FriendTo> getFriendsForView(AuthUser user) {
        return filterFriends(findFriends(user), groupService.getFilterGroups(user.getUser()), user);
    }

    private List<UserXtrCounters> findFriends(AuthUser user) {
        List<String> friendsId = findFriendsId(user);
        List<UserXtrCounters> result = new ArrayList<>();
        int count = 0;
        while (count <= friendsId.size()) {
            count += 1000;
            result.addAll(VkQueries.getFullUsers(user, friendsId.subList(count, count + 1000)));
        }
        return result;
    }

    private List<String> findFriendsId(AuthUser user) {
        List<Integer> result = new ArrayList<>();
        int count;
        int loop = 0;
        do {
            List<Integer> items = VkQueries.findFriendsId(user, loop);
            count = items.size();
            loop++;
            result.addAll(items);
        } while (count == 5000);
        return result.stream().map(String::valueOf).collect(Collectors.toList());
    }

    private List<FriendTo> filterFriends(List<UserXtrCounters> friends, List<Group> filterGroups, AuthUser user) {
        Map<UserXtrCounters, List<Group>> mapForTransform = new HashMap<>();
        DateFormat format = new SimpleDateFormat("d.M.yyyy", Locale.ENGLISH);
        Calendar now = Calendar.getInstance();
        List<Invite> invites = inviteService.getUserInvites(user.getId());
        for (Group group : filterGroups) {
            VkQueries.join(user.getActor(), group.getGroupId());
            Set<Integer> groupInvites = invites.stream()
                    .filter(i -> i.getGroupId() == group.getGroupId())
                    .map(Invite::getInvitedId)
                    .collect(Collectors.toSet());
            List<UserXtrCounters> filterToGroupFriends = filterToGroupFields(friends, group, groupInvites, format, now.get(Calendar.YEAR));
            List<UserXtrCounters> filterGroupMembers = filterGroupMembers(user, filterToGroupFriends, group.getGroupId());
            prepareTransformFriends(group, filterGroupMembers, mapForTransform);
        }
        return transformFriends(mapForTransform, user);
    }

    private int getYearOld(DateFormat format, UserXtrCounters friend, int yearNow) {
        if (friend.getBdate().length() < 8 || friend.getBdate().length() > 10) {
            Calendar bdDay = Calendar.getInstance();
            try {
                bdDay.setTime(format.parse(friend.getBdate()));
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
            int bdYear = bdDay.get(Calendar.YEAR);
            if (bdYear < 1900 || bdYear > 2030) {
                return 0;
            }
            return yearNow - bdYear;
        } else return 0;
    }

    private List<UserXtrCounters> filterGroupMembers(AuthUser user, List<UserXtrCounters> friends, int groupId) {
        int count = 0;
        List<Integer> friendsId = friends.stream().map(UserMin::getId).collect(Collectors.toList());
        List<MemberStatus> memberStatuses = new ArrayList<>();
        while (count < friends.size()) {
            List<Integer> subList = friendsId.subList(count, count + 500 < friendsId.size() ? count + 500 : friendsId.size() - 1);
            memberStatuses.addAll(VkQueries.getMembersStatus(user, subList, groupId));
            count += 500;
        }
        Set<Integer> noMembersId = memberStatuses.stream()
                .filter(i -> !i.isMember())
                .map(MemberStatus::getUserId)
                .collect(Collectors.toSet());
        return friends.stream().filter(i -> noMembersId.contains(i.getId())).collect(Collectors.toList());
    }

    private List<UserXtrCounters> filterToGroupFields(List<UserXtrCounters> friends, Group group, Set<Integer> invites, DateFormat format, int yearNow) {
        Set<String> cities = new HashSet<>(Arrays.asList(group.getCities().split(",")));
        return friends.stream()
                .filter(i -> (group.getCities() == null
                        || group.getCities().length() == 0
                        || (i.getCity() != null && i.getCity().getTitle() != null && cities.contains(i.getCity().getTitle()))
                        || (i.getHomeTown() != null && cities.contains(i.getHomeTown())))
                        &&
                        (group.getSex() == null
                                || (i.getSex() != null && group.getSex().equals(i.getSex())))
                        &&
                        (group.getMinAge() == null || getYearOld(format, i, yearNow) >= group.getMinAge())
                        &&
                        ((group.getMaxAge() == null || getYearOld(format, i, yearNow) <= group.getMaxAge()))
                        &&
                        !invites.contains(i.getId())
                ).collect(Collectors.toList());
    }

    private void prepareTransformFriends(Group group, List<UserXtrCounters> listForTransform, Map<UserXtrCounters, List<Group>> mapForTransform) {
        listForTransform.forEach(i -> {
            List<Group> groups = mapForTransform.get(i);
            if (groups == null) {
                mapForTransform.put(i, new ArrayList<>(Collections.singletonList(group)));
            } else {
                groups.add(group);
            }
        });
    }

    private List<FriendTo> transformFriends(Map<UserXtrCounters, List<Group>> mapForTransform, AuthUser user) {
        return mapForTransform.entrySet().stream()
                .map(entry -> new FriendTo(
                        entry.getKey().getId(),
                        entry.getKey().getPhoto50(),
                        getUserVkName(entry.getKey()),
                        Utils.convertGroupsToGroupsTo(user, entry.getValue()))
                ).collect(Collectors.toList());
    }
}
