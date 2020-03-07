//package com.luga_online.controller;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.luga_online.model.User;
//import com.luga_online.service.AuthService;
//import com.luga_online.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
////import ru.javaops.model.User;
////import ru.javaops.service.AuthService;
////import ru.javaops.service.UserService;
////import ru.javaops.to.UserToExt;
////import ru.javaops.util.UserUtil;
////import ru.javaops.util.WebUtil;
////import ru.javaops.util.exception.ApplicationException;
////import ru.javaops.web.exception.ErrorType;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.URI;
//import java.util.Optional;
//
//import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
////import static ru.javaops.util.WebUtil.CURRENT_URL;
////import static ru.javaops.util.WebUtil.PRE_AUTHORIZED;
//
//@Slf4j
//public abstract class AbstractOAuth2Controller {
//
//    @Autowired
//    protected RestTemplate template;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private RequestCache requestCache;
//
//    final OAuth2Provider provider;
//
//    public AbstractOAuth2Controller(OAuth2Provider provider) {
//        this.provider = provider;
//    }
//
//    @GetMapping("/callback")
//    public void authenticate(HttpServletRequest req, HttpServletResponse resp,
//                             @RequestParam(required = false) String code, @RequestParam(required = false) String state,
//                             @RequestParam(value = "error_description", required = false) String errorDescription,
//                             @CookieValue(value = CURRENT_URL, required = false) String currentUrl) throws IOException {
//        if (!StringUtils.isEmpty(errorDescription)) {
//            throw new ApplicationException(errorDescription, ErrorType.AUTH_ERROR);
//        }
//        if (StringUtils.isEmpty(code) || !state.equals("csrf_token_auth")) {
//            log.error("Ошибка авторизации, запрос: {}", WebUtil.printRequest(req));
//            throw new ApplicationException("Пожалуйста, выйдите из аккаунта " + provider.getName() + " перед логином", ErrorType.AUTH_ERROR);
//        }
//
//        JsonNode response = getAccessToken(code);
//        UserToExt userToExt = getUserToExt(response);
//        if (StringUtils.isEmpty(userToExt.getEmail())) {
//            throw new ApplicationException("Невозможно получить email из профиля", ErrorType.AUTH_ERROR);
//        }
//        log.info(provider.getName() + " authorization from user {}", userToExt.getEmail());
//        User user = userService.findByEmailOrGmail(userToExt.getEmail());
//        if (user == null) {
//            WebUtil.setInSession(PRE_AUTHORIZED, userToExt);
//            resp.sendRedirect("/view/message/createNew?email=" + userToExt.getEmail());
//            return;
//        }
//        if (UserUtil.updateFromAuth(user, userToExt)) {
//            user = userService.save(user);
//        }
//        authService.setAuthorized(user);
//        WebUtil.redirect(requestCache, req, resp, currentUrl);
//    }
//
//    protected Optional<String> getFromResponse(JsonNode response, String name) {
//        return Optional.ofNullable(response.get(name)).map(JsonNode::asText).map(v -> "null".equals(v) ? null : v);
//    }
//
//    protected String getFromResponseOrNull(JsonNode response, String name) {
//        return getFromResponse(response, name).orElse(null);
//    }
//
//    protected String getFromResponseOrThrow(JsonNode response, String name, String exceptionMsg) {
//        return getFromResponse(response, name).orElseThrow(() -> new ApplicationException(exceptionMsg, ErrorType.AUTH_ERROR));
//    }
//
//    protected String getAccessToken(JsonNode response) {
//        return getFromResponseOrThrow(response, "access_token", "Ошибка получения access_token");
//    }
//
//    protected abstract UserToExt getUserToExt(JsonNode response);
//
//    @GetMapping
//    public String authorize() {
//        return "redirect:" + provider.getAuthorizeUrl()
//                + "?client_id=" + provider.getClientId()
//                + "&redirect_uri=" + provider.getRedirectUri()
//                + "&scope=" + provider.getScope()
//                + "&state=csrf_token_auth";
//    }
//
//    protected JsonNode getAccessToken(String code) {
//        URI uri = fromHttpUrl(provider.getAccessTokenUrl())
//                .queryParam("client_id", provider.getClientId())
//                .queryParam("client_secret", provider.getClientSecret())
//                .queryParam("code", code)
//                .queryParam("redirect_uri", provider.getRedirectUri())
//                .queryParam("grant_type", "authorization_code").build().encode().toUri();
//
//        ResponseEntity<JsonNode> tokenEntity = template.postForEntity(uri, null, JsonNode.class);
//        return tokenEntity.getBody();
//    }
//
//    protected JsonNode getUserInfo(String url, String accessToken) {
//        UriComponentsBuilder builder = fromHttpUrl(url).queryParam("access_token", accessToken);
//        return doGet(builder);
//    }
//
//    protected JsonNode doGet(UriComponentsBuilder builder) {
//        ResponseEntity<JsonNode> entity = template.getForEntity(builder.build().encode().toUri(), JsonNode.class);
//        return entity.getBody();
//    }
//}
