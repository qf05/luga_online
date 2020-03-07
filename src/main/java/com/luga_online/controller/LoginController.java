//package com.luga_online.controller;
//
//import com.luga_online.util.Properties;
//import com.yandex.money.api.authorization.AuthorizationData;
//import com.yandex.money.api.authorization.AuthorizationParameters;
//import com.yandex.money.api.methods.Token;
//import com.yandex.money.api.model.Scope;
//import com.yandex.money.api.net.AuthorizationCodeResponse;
//import com.yandex.money.api.net.clients.ApiClient;
//import com.yandex.money.api.net.clients.DefaultApiClient;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.social.connect.ConnectionRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.inject.Inject;
//import java.security.Principal;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Controller
//@EnableAutoConfiguration
//public class LoginController {
//
////    @Inject
////    private ConnectionRepository connectionRepository;
////
////    @RequestMapping(value="/vkontakte", method=RequestMethod.GET)
////    public String home(Model model) {
////        Connection<VKontakte> connection = connectionRepository.findPrimaryConnection(VKontakte.class);
////        if (connection == null) {
////            return "redirect:/connect/vkontakte";
////        }
////        model.addAttribute("profile", connection.getApi().usersOperations().getUser());
////        return "vkontakte/profile";
////    }
//
//    @RequestMapping(value = { "/user", "/me"}, method = RequestMethod.GET)
//    public Map<String, String> user(Principal principal) {
//        Map<String, Object> details = (Map<String, Object>) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("name", (String) details.get("name"));
//        map.put("email", (String) details.get("email"));
//        return map;
//    }
//
//    @GetMapping("/loginVk")
//    public String loginVk(){
//        return "";
//    }
//
//    @PostMapping("/loginVk")
//    public String loginVkPost(){
//        return "";
//    }
//
//    @GetMapping("/login/vk")
//    public String loginVk1(){
//        return "";
//    }
//
//    @PostMapping("/login/vk")
//    public String loginVkPost1(){
//        return "";
//    }
//
//    @GetMapping("/login")
//    public String login(){
//        ApiClient client = new DefaultApiClient.Builder()
//                .setClientId(Properties.YANDEX_APP_ID)
//                .create();
//        AuthorizationParameters parameters = new AuthorizationParameters.Builder()
//                .addScope(Scope.ACCOUNT_INFO)
//                .addScope(Scope.PAYMENT_P2P)
//                .setRedirectUri("localhost:8080")
//                .setResponseType("code")
//                .create();
//        AuthorizationData data = client.createAuthorizationData(parameters);
//        String url = data.getUrl() + "?" + new String(data.getParameters());
//
//        return url;
//    }
//
//    @PostMapping("/login")
//    public String ya(){
//        ApiClient client = new DefaultApiClient.Builder()
//                .setClientId(Properties.YANDEX_APP_ID)
//                .create();
//        AuthorizationParameters parameters = new AuthorizationParameters.Builder()
//                .addScope(Scope.ACCOUNT_INFO)
//                .addScope(Scope.PAYMENT_P2P)
//                .setRedirectUri("localhost:8080")
//                .setResponseType("code")
//                .create();
//        AuthorizationData data = client.createAuthorizationData(parameters);
//        String url = data.getUrl() + "?" + new String(data.getParameters());
//
//
//
////        String  url = "https://oauth.vk.com/authorize?client_id=" +
////        APP_ID.toString() + "&redirect_url=" + BLANK +
////        "&group_ids=90800156&display=page&response_type=token&scope=messages&v=5.92";
////        resp.sendRedirect(url);
//        return url;
//    }
//}
