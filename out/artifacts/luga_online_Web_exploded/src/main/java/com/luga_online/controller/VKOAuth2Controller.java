//package com.luga_online.controller;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/login/vk")
//public class VKOAuth2Controller extends AbstractOAuth2Controller {
//
//    @Autowired
//    public VKOAuth2Controller(@Qualifier("vkOAuth2Provider") OAuth2Provider provider) {
//        super(provider);
//    }
//
//    @Override
//    public String authorize() {
//        return super.authorize() + "&response_type=code&v=5.95&display=page";
//    }
//
//    @Override
//    protected UserToExt getUserToExt(JsonNode response) {
//        String accessToken = getAccessToken(response);
//        String email = getFromResponseOrThrow(response, "email", "Невозможно получить email из профиля VK");
//        JsonNode jsonResponse = getUserInfo(provider.getUserInfoUrl(), accessToken);
//        JsonNode node = jsonResponse.get("response").get(0);
//        String name = getFromResponseOrNull(node, "first_name");
//        if (name != null) {
//            name += ' ' + getFromResponse(node, "last_name").orElse("");
//        }
//        UserToExt userToExt = new UserToExt(email, name);
//        userToExt.setVk("https://vk.com/id" + node.get("id").asInt());
//        return userToExt;
//    }
//}
