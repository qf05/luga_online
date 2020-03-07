//package com.luga_online.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.social.UserIdSource;
//import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
//import org.springframework.social.config.annotation.SocialConfigurer;
//import org.springframework.social.connect.ConnectionFactoryLocator;
//import org.springframework.social.connect.UsersConnectionRepository;
//
//@Configuration
//public class SocialConfig implements SocialConfigurer {
//
//    @Override
//    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
//        cfConfig.addConnectionFactory(new VKontakteConnectionFactory(
//                env.getProperty("vkontakte.clientId"),
//                env.getProperty("vkontakte.clientSecret")));
//    }
//
//
//    @Override
//    public UserIdSource getUserIdSource() {
//        return null;
//    }
//
//    @Override
//    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
//        return null;
//    }
//}
