package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Group;
import com.luga_online.model.Invite;
import com.luga_online.to.FriendTo;
import com.luga_online.util.Utils;
import com.luga_online.util.VkQueries;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.MemberStatus;
import com.vk.api.sdk.objects.users.User;
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
import static com.luga_online.util.Utils.pauseVk;

@Service
@Slf4j
public class FriendUtils {

    private final GroupService groupService;

    private final InviteService inviteService;

    private final VkApiClient vk;

    @Autowired
    public FriendUtils(GroupService groupService, InviteService inviteService, VkApiClient vk) {
        this.groupService = groupService;
        this.inviteService = inviteService;
        this.vk = vk;
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


    private List<FriendTo> filterFriendsOld(List<UserXtrCounters> friends, List<Group> filterGroups, AuthUser user) {
        DateFormat format = new SimpleDateFormat("d.M.yyyy", Locale.ENGLISH);
        Calendar now = Calendar.getInstance();
        Map<Group, List<Integer>> map = new HashMap<>();
        for (Group group : filterGroups) {
            List<String> cities = Arrays.asList(group.getCities().split(","));
            Set<Integer> invites = inviteService.getInvitesToGroup(user.getId(), group.getGroupId());
            List<Integer> result = friends.stream()
                    .filter(i -> invites.contains(i.getId()))
                    .filter(i -> group.getCities() == null
                            || group.getCities().length() == 0
                            || (i.getCity() != null && i.getCity().getTitle() != null && cities.contains(i.getCity().getTitle()))
                            || (i.getHomeTown() != null && cities.contains(i.getHomeTown())))
                    .filter(i -> group.getSex() == null || group.getSex().equals(i.getSex()))
                    .filter(i -> {
                        if (group.getMinAge() == null && group.getMaxAge() == null) {
                            return true;
                        } else {
                            int year;
                            if (i.getBdate().length() < 8 || i.getBdate().length() > 10) {
                                Calendar bdDay = Calendar.getInstance();
                                try {
                                    bdDay.setTime(format.parse(i.getBdate()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                                int bdYear = bdDay.get(Calendar.YEAR);
                                if (bdYear < 1900 || bdYear > 2030) {
                                    return false;
                                }
                                year = now.get(Calendar.YEAR) - bdYear;
                            } else return false;
                            return (group.getMinAge() == null || group.getMinAge() >= year)
                                    && (group.getMaxAge() == null || group.getMaxAge() <= year);
                        }
                    }).map(UserMin::getId)
                    .collect(Collectors.toList());
            map.put(group, result);
        }
        for (Group group : map.keySet()) {
            List<Integer> usersId = map.get(group);
            int count = 0;
            List<MemberStatus> execute = new ArrayList<>();
            while (count <= usersId.size()) {
                try {
                    execute = vk.groups()
                            .isMember(user.getActor(), String.valueOf(group.getGroupId()),
                                    usersId.subList(count, count + 500 < usersId.size() ? count + 500 : usersId.size() - 1))
                            .execute();
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                    continue;
                }
                pauseVk();
                count += 500;
            }
            execute.forEach(i -> {
                if (i.isMember()) {
                    usersId.remove(i.getUserId());
                }
                map.put(group, usersId);
            });
        }

        return friends.stream().map(i -> {
            List<Group> groups = new ArrayList<>();
            for (Group group : map.keySet()) {
                if (map.get(group).contains(i.getId())) {
                    groups.add(group);
                }
            }
            if (groups.size() > 0) {
                String name = i.getFirstName() + " " + i.getLastName();
                return new FriendTo(i.getId(), i.getPhoto50(), name, Utils.convertGroupsToGroupsTo(user, groups));
            } else return null;
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<FriendTo> filterFriendsMadeByIrina(List<UserXtrCounters> friends, List<Group> filterGroups, AuthUser user) {
        DateFormat format = new SimpleDateFormat("d.M.yyyy", Locale.ENGLISH);
        Calendar now = Calendar.getInstance();

        Map<UserXtrCounters, List<Group>> mapForTransform = new HashMap<>();
//        Map<Group, List<UserXtrCounters>> mapForTransform2 = new HashMap<>();

        Map<String, List<UserXtrCounters>> citiesMap = friends.stream()
                .collect(Collectors.groupingBy(e -> e.getCity().getTitle()));

        for (Group group : filterGroups) {
            Set<Integer> invites = inviteService.getInvitesToGroup(user.getId(), group.getGroupId());
            List<UserXtrCounters> friendsToCities = new ArrayList<>();
            if (group.getCities() != null && group.getCities().length() > 0) {
                List<String> cities = Arrays.asList(group.getCities().split(","));
                cities.forEach(i -> friendsToCities.addAll(citiesMap.get(i)));
            } else {
                friendsToCities.addAll(friends);
            }

            List<UserXtrCounters> friendToSex = new ArrayList<>();
            if (group.getSex() != null) {
                Map<Integer, List<UserXtrCounters>> sexMap = friendsToCities.stream()
                        .collect(Collectors.groupingBy(User::getSex));
                friendToSex.addAll(sexMap.get(group.getSex()));
            } else {
                friendToSex.addAll(friendsToCities);
            }
            List<UserXtrCounters> friendToAge = new ArrayList<>();
            if (group.getMinAge() != null && group.getMaxAge() != null) {
                Map<Integer, List<UserXtrCounters>> yearMap = friendToSex.stream()
                        .collect(Collectors.groupingBy(i -> getYearOld(format, i, now.get(Calendar.YEAR))));
                yearMap.keySet().forEach(i -> {
                    if ((group.getMinAge() == null || group.getMinAge() >= i)
                            && (group.getMaxAge() == null || group.getMaxAge() <= i)) {
                        friendToAge.addAll(yearMap.get(i));
                    }
                });
            } else {
                friendToAge.addAll(friendToSex);
            }
            List<UserXtrCounters> wasInviteFilterFriends = friendToAge.stream().filter(i -> invites.contains(i.getId())).collect(Collectors.toList());
            List<UserXtrCounters> filterFriends = filterGroupMembers(user, wasInviteFilterFriends, group.getGroupId());
            prepareTransformFriends(group, filterFriends, mapForTransform);
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
            if (group == null) {
                groups = new ArrayList<>();
            }
            groups.add(group);
            mapForTransform.put(i, groups);
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

//    private List<FriendTo> transformFriends2(Map<Group, Set<UserXtrCounters>> mapForTransform) {
//        Set<UserXtrCounters> values = mapForTransform.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
//        return values.stream().map(i -> {
//            List<Group> groups = new ArrayList<>();
//            for (Group group : mapForTransform.keySet()) {
//                if (mapForTransform.get(group).contains(i)) {
//                    groups.add(group);
//                }
//            }
//            if (groups.size() > 0) {
//                String name = i.getFirstName() + " " + i.getLastName();
//                return new FriendTo(i.getId(), i.getPhoto50(), name, groups);
//            } else return null;
//        }).filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
}
