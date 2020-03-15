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
}
