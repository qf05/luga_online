package com.luga_online;

import com.luga_online.repository.UserRepository;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;

@SpringBootApplication
@AllArgsConstructor
public class LugaOnlineApplication implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(LugaOnlineApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(userRepository.findAll());
    }

    @Qualifier("oauth2ClientContext")
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    public OAuth2ClientContext oAuth2ClientContext() {
        return oauth2ClientContext;
    }

    @Bean
    public VkApiClient vk() {
        return new VkApiClient(HttpTransportClient.getInstance());
    }
}
