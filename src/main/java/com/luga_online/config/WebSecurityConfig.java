package com.luga_online.config;

//import com.luga_online.util.UserInfoTokenServicesForVk;
//import com.luga_online.util.VkCustomFilter;
//import com.luga_online.util.UserInfoTokenServicesForVk;

import com.luga_online.service.AuthService;
import com.luga_online.util.VkCustomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.luga_online.util.Properties.VK_APP_ID;
import static com.luga_online.util.Properties.VK_CLIENT_SECRET;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
//@EnableOAuth2Sso
@EnableAuthorizationServer
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private AuthService authService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
//                .authorizeRequests()
//                .mvcMatchers("/", "/login")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/user"))
                .and()
                .logout()
                .logoutSuccessUrl("/user")
                .permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .and()
                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }


    @Bean
    public AuthorizationCodeResourceDetails vkAuth() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
//        details.setClientId("6785724");
//        details.setClientSecret("4NuxUQwll8PWsDEr2uKY");
        details.setClientId(VK_APP_ID);
        details.setClientSecret(VK_CLIENT_SECRET);
        details.setAccessTokenUri("https://oauth.vk.com/access_token");
        details.setAuthenticationScheme(AuthenticationScheme.form);
        ;
        details.setScope(Arrays.asList("friends", "groups", "offline"));
        details.setClientAuthenticationScheme(AuthenticationScheme.query);
        details.setPreEstablishedRedirectUri("http://localhost:8080/login");
        details.setUserAuthorizationUri("https://oauth.vk.com/authorize");
        details.setTokenName("vk");
        return details;
    }

//    @Bean
////    @ConfigurationProperties("vk.resource")
//    public ResourceServerProperties vkResource() {
//        ResourceServerProperties properties = new ResourceServerProperties();
//        properties.setUserInfoUri("https://api.vk.com/method/users.get");
//        return properties;
//    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    private Filter ssoFilter() {
        VkCustomFilter vkFilter = new VkCustomFilter(authService);
        OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vkAuth(), oauth2ClientContext);
        vkFilter.setRestTemplate(vkTemplate);
//        UserInfoTokenServices tokenServices = new UserInfoTokenServices(vkResource().getUserInfoUri(), vk().getClientId());
//        UserInfoTokenServicesForVk tokenServices = new UserInfoTokenServicesForVk(vkResource().getUserInfoUri(), vk().getClientId());
////        UserInfoTokenServicesForVk tokenServices = new UserInfoTokenServicesForVk("http://localhost:8080", "6785724");
//        tokenServices.setRestTemplate(vkTemplate);
//        vkFilter.setTokenServices(tokenServices);
//        filters.add(vkFilter);
//
//        filter.setFilters(filters);
//        return filter;
        return vkFilter;
    }
}
