package com.luga_online.service;

import com.luga_online.model.AuthUser;
import com.luga_online.model.Role;
import com.luga_online.model.User;
import com.luga_online.repository.UserRepository;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
//@Slf4j
@AllArgsConstructor
//@NoArgsConstructor
public class AuthService {

    @Autowired
    private final UserService userService;

    @Autowired
    private final VkApiClient vk;

//    private final UserRepository userRepository;

//    AuthenticationManager authenticationManager;

//    public void setAuthorized(String email) {
//        setAuthorized(userService.findExistedByEmail(email));
//    }
//
//    public void setAuthorized(User user) {
//        setAuthorized(user, WebUtil.getRequest());
//    }

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
        UserXtrCounters userInfo = null;
        try {
            userInfo = vk.users().get(actor).fields(UserField.PHOTO_100).execute().get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }

        if (userInfo != null) {
            name = userInfo.getLastName() + " " + userInfo.getFirstName() + " " + userInfo.getScreenName();
            photo = userInfo.getPhoto100();
        } else {
            throw new RuntimeException();
        }

        AuthUser authUser = new AuthUser(user, actor, name, photo);


////        AuthUser authUser = AuthorizedUser.user();
//        if (user.equals(authUser)) {
//            return;
//        }
////        log.info("setAuthorized for '{}', '{}'", user.getEmail(), user.getFullName());
//        if (authUser != null) {
//            request.getSession(false).invalidate();
//        }
////        authUser = createAuth(user);

        // Authenticate the user
//        Authentication authentication = authenticationManager.authenticate(authRequest);
//        Collection<? extends GrantedAuthority> authorities

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, null,
                        Collections.singleton(authUser.getUser().getRole())));

// Create a new session and add the security context.
// https://stackoverflow.com/a/8336233/548473
//        log.debug("Create session for {}", user);
        HttpSession session = request.getSession(true);
//        session.removeAttribute(WebUtil.PRE_AUTHORIZED);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        return context.getAuthentication();
    }
}
