package com.luga_online.model;

import com.vk.api.sdk.client.actors.UserActor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
public class AuthUser implements Serializable {

    private User user;
    private UserActor actor;
    private String userName;
    private String photo;
    private int id;
//    private VkApiClient vk;

//    public AuthUser(User user, UserActor actor, String userName, String photo) {
//        this.user = user;
//        this.actor = actor;
//        this.userName = userName;
//        this.photo = photo;
////        this.vk = new VkApiClient(HttpTransportClient.getInstance());
////        Integer id = Integer.parseInt(user.getVkId().replaceAll("\"",""));
////        this.actor = new UserActor(user.getVkId(), token);
//    }

    //    public static UserActor actor = null;
//    public static TransportClient transportClient = HttpTransportClient.getInstance();
//    public static VkApiClient vk = new VkApiClient(transportClient);
//    actor = new UserActor(ID, TOKEN);
}
