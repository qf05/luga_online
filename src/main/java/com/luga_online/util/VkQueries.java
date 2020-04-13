package com.luga_online.util;

import com.luga_online.model.AuthUser;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.base.responses.OkResponse;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.groups.MemberStatus;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;

import java.util.ArrayList;
import java.util.List;

import static com.luga_online.util.Utils.pauseVk;

public class VkQueries {

    public static final VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());

    public static UserXtrCounters getUserInfo(UserActor actor) {
        UserXtrCounters userInfo = null;
        try {
            userInfo = vk.users().get(actor).fields(UserField.PHOTO_50, UserField.PHOTO_100).execute().get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        pauseVk();
        return userInfo;
    }

    public static List<UserXtrCounters> getFullUsers(AuthUser user, List<String> usersId) {
        List<UserXtrCounters> items = new ArrayList<>();
        try {
            items = vk.users()
                    .get(user.getActor())
                    .fields(UserField.PHOTO_50,
                            UserField.CITY,
                            UserField.HOME_TOWN,
                            UserField.BDATE,
                            UserField.SEX)
                    .userIds(usersId)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        pauseVk();
        return items;
    }

    public static List<Integer> findFriendsId(AuthUser user, int loop) {
        List<Integer> items = new ArrayList<>();
        try {
            items = vk.friends()
                    .get(user.getActor())
                    .offset(loop * 5000)
                    .count(5000)
                    .execute()
                    .getItems();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        pauseVk();
        return items;
    }

    public static List<MemberStatus> getMembersStatus(AuthUser user, List<Integer> friendsId, int groupId) {
        List<MemberStatus> memberStatuses = new ArrayList<>();
        try {
            memberStatuses = vk.groups()
                    .isMember(user.getActor(), String.valueOf(groupId), friendsId)
                    .execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        pauseVk();
        return memberStatuses;
    }

    public static Object[] inviteFriendToGroup(AuthUser user, int friendId, int groupId) {
        Object[] result = new Object[2];
        Integer resultCode = 0;
        try {
            resultCode = vk.groups().invite(user.getActor(), groupId, friendId).execute().getValue();
            if (resultCode == 1) {
                result[1] = "OK";
            } else {
                result[1] = "error";
            }
        } catch (ApiException e) {
            resultCode = e.getCode();
            result[1] = e.getMessage();
        } catch (ClientException e) {
            result[1] = e.getMessage();
        }
        result[0] = resultCode;
        pauseVk();
        return result;
    }

    //не более 500 групп
    public static List<GroupFull> getGroups(AuthUser user, List<String> groupsId) {
        List<GroupFull> items = new ArrayList<>();
        try {
            items = vk.groups().getById(user.getActor()).groupIds(groupsId).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        pauseVk();
        return items;
    }

    public static void join(UserActor user, Integer groupId) {
        try {
            BoolInt isMember = vk.groups().isMember(user, String.valueOf(groupId)).execute();
            pauseVk();
            if (isMember.getValue() == 0) {
                vk.groups().join(user).groupId(groupId).execute();
                pauseVk();
            }
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(UserActor user) {
//        String accessToken;
//        int vkAppId = Integer.parseInt(VK_APP_ID);

//        String s = "https://oauth.vk.com/access_token?client_id=" + VK_APP_ID + "&client_secret=" + VK_SERVES_KEY + "&v=5.103&grant_type=client_credentials";
        try {
//            vk.oauth().
            OkResponse execute = vk.groups().invite(user, 78189411, 1350733).execute();
            vk.groups().join(user).groupId(90800156).execute();
            pauseVk();
            Integer dcs = vk.messages().send(user).message("dcs").userId(90800156).execute();
            System.out.println(dcs);

//            accessToken = vk.oauth().serverClientCredentionalsFlow(vkAppId, VK_CLIENT_SECRET).execute().getAccessToken();
//
//            ServerActor serverActor = new ServerActor(vkAppId, accessToken);


//            List<Integer> re = vk.secure()
//                    .sendNotification(serverActor, "RE RE")
//                    .userId(1350733)
//                    .unsafeParam("client_secret",VK_CLIENT_SECRET).execute();
//            OkResponse re_re = vk.secure().sendSMSNotification(serverActor, 1350733, "RE RE")
//                    .unsafeParam("client_secret",VK_CLIENT_SECRET).execute();
//            re.forEach(System.out::println);
//            System.out.println("fdhngk   -   "+re_re.getValue());
//            System.out.println("fdhngk2   -   "+re_re.getValue());
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
//        vk.messages().send(user.getActor()).message("").userIds(1,2).execute();
//        vk.notifications().markAsViewed(user.getActor()).execute();
//
//        vk.notifications().
    }
}
