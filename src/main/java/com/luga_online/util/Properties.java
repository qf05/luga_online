package com.luga_online.util;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Properties {

    public static String YANDEX_TOKEN;
    public static final String YANDEX_APP_ID;
    public static final String YANDEX_REDIRECT_URI = "http://localhost:8080/ya";

    public static final String VK_APP_ID;
    public static final String VK_CLIENT_SECRET;
    public static final String VK_ACCESS_TOKEN_URI = "https://oauth.vk.com/access_token";
    public static final List<String> VK_SCOPE = Arrays.asList("friends", "groups", "offline");
    public static final String VK_PRE_ESTABLISHED_REDIRECT_URI = "http://localhost:8080/login";
    public static final String VK_USER_AUTHORIZATION_URI = "https://oauth.vk.com/authorize";
    public static final String VK_TOKEN_NAME = "vk";

    static {
        Resource resource = new ClassPathResource("config/password.properties");
        try {
            PropertySource<Map<String, Object>> propertySource = new ResourcePropertySource(resource);
            YANDEX_APP_ID = (String) propertySource.getProperty("yandex.app.id");
            YANDEX_TOKEN = (String) propertySource.getProperty("yandex.client.token");
            VK_APP_ID = (String) propertySource.getProperty("vk.app.id");
            VK_CLIENT_SECRET = (String) propertySource.getProperty("vk.client.secret");

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
