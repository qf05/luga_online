package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.User;
import com.luga_online.util.VkQueries;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;

import static com.luga_online.util.Utils.getUserVkName;

@Service
@Slf4j
public class AuthService {

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Authentication setAuthorized(OAuth2AccessToken accessToken, HttpServletRequest request) {
        Integer user_id = (Integer) accessToken.getAdditionalInformation().get("user_id");
        User user = userService.findUser(user_id);
        if (user == null) {
            user = userService.createUser(user_id);
        }
        return setAuthorized(user, accessToken.getValue(), request);
    }

    public Authentication setAuthorized(User user, String token, HttpServletRequest request) {
        String name;
        String photo;
        UserActor actor = new UserActor(user.getVkId(), token);
        UserXtrCounters userInfo = VkQueries.getUserInfo(actor);
        if (userInfo != null) {
            name = getUserVkName(userInfo);
            photo = userInfo.getPhoto50();
            if (photo == null) {
                photo = userInfo.getPhoto100();
            }
        } else {
            throw new RuntimeException();
        }

        AuthUser authUser = new AuthUser(user, actor, name, photo, user.getVkId());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, null,
                        Collections.singleton(authUser.getUser().getRole())));
        HttpSession session = request.getSession(true);
//        session.removeAttribute(WebUtil.PRE_AUTHORIZED);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        return context.getAuthentication();
    }
}
