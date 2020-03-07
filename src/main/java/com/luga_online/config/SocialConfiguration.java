//package com.luga_online.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.social.UserIdSource;
//import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
//import org.springframework.social.config.annotation.EnableSocial;
//import org.springframework.social.config.annotation.SocialConfigurer;
//import org.springframework.social.connect.ConnectionFactoryLocator;
//import org.springframework.social.connect.UsersConnectionRepository;
//import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
//import org.springframework.social.connect.web.ProviderSignInUtils;
//import org.springframework.social.security.AuthenticationNameUserIdSource;
//import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;
//
//import javax.sql.DataSource;
//
//@EnableSocial
//@Configuration
//@RequiredArgsConstructor
//public class SocialConfiguration implements SocialConfigurer {
////    private final DataSource dataSource;
////    private final Environment environment;
//    private final VkontakteEnvironment vkEnv;
////    private final SocialEncryptEnvironment encryptEnv;
////    private final ConnectionSignUp connectionSignUpService;
//
//
//    @Override
//    public void addConnectionFactories(ConnectionFactoryConfigurer connectionCfg, Environment env) {
//        connectionCfg.addConnectionFactory(new VKontakteConnectionFactory(vkEnv.getClientId(), vkEnv.getClientSecret()));
//    }
//
//
//    @Override
//    public UserIdSource getUserIdSource() {
//        return new AuthenticationNameUserIdSource();
//    }
//
//    @Override
//    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator locator) {
//
//        return new InMemoryUsersConnectionRepository(locator);
////        if(Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
////            Assert.hasText(encryptEnv.getKey(), "in production auth tokens must be secured");
////        }
////
////        TextEncryptor encryptor = hasText(encryptEnv.getKey()) ? text(encryptEnv.getKey(), encryptEnv.getSalt()) : noOpText();
////        JdbcUsersConnectionRepository connectionRepository = new JdbcUsersConnectionRepository(dataSource, locator, encryptor);
////        connectionRepository.setConnectionSignUp(connectionSignUpService);
////        return connectionRepository;
//    }
//
//    @Bean
//    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator locator, UsersConnectionRepository repository) {
//        return new ProviderSignInUtils(locator, repository);
//    }
//
//}
