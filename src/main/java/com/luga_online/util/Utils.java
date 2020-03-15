package com.luga_online.util;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Group;
import com.luga_online.to.GroupTo;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.users.UserXtrCounters;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static String convertMoney(Long money) {
        return String.format("%.2f", (double) money / 100.0);
    }

    public static void pauseVk() {
        try {
            Thread.sleep(335);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getUserVkName(UserXtrCounters user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    //не более 500 групп
    public static List<GroupTo> convertGroupsToGroupsTo(AuthUser user, List<Group> groups) {
        List<String> groupsId = groups.stream().map(i -> String.valueOf(i.getGroupId())).collect(Collectors.toList());
        Map<Integer, String> priceMap = groups.stream().collect(Collectors.toMap(Group::getGroupId, i -> convertMoney(i.getPrice())));
        List<GroupFull> groupFulls = VkQueries.getGroups(user, groupsId);
        return groupFulls.stream()
                .map(i -> {
                    int id = Integer.parseInt(i.getId());
                    return new GroupTo(id, i.getName(), priceMap.get(id), i.getPhoto50());
                }).collect(Collectors.toList());

    }
}
