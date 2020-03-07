package com.luga_online.util;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class Properties {

    public static String YANDEX_TOKEN;
    public static final String YANDEX_APP_ID;

    public static final String VK_APP_ID;
    public static final String VK_REDIRECT_URI = "http://localhost:8080/loginVk";
    public static final String VK_CLIENT_SECRET;


    static {
        Resource resource = new ClassPathResource("config/password.properties");
        try {
            PropertySource propertySource = new ResourcePropertySource(resource);
            YANDEX_APP_ID = (String) propertySource.getProperty("yandex.app.id");
            VK_APP_ID = (String) propertySource.getProperty("vk.app.id");
            VK_CLIENT_SECRET = (String) propertySource.getProperty("vk.client.secret");

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
