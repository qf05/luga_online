//package com.luga_online.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//import org.springframework.social.security.SpringSocialConfigurer;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
////    @Value("${server.servlet.session.cookie.name")
////    private String cookieSessionName;
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/login")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//
////        http
////                .antMatcher("/**")
////                .authorizeRequests()
////                .antMatchers("/", "/login**", "/webjars/**")
////                .permitAll()
////                .anyRequest()
////                .authenticated()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/ll"))
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .permitAll()
//                .and()
//                .csrf().disable()
//                .apply(new SpringSocialConfigurer());
////                .csrfTokenRepository(csrfTokenRepository())
////                .and()
////                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
////                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
//
//
////        http
////            .logout()
////                .logoutRequestMatcher(new AntPathRequestMatcher("/signout"))
////                .deleteCookies(cookieSessionName)
////            .and()
////                .authorizeRequests()
////                .antMatchers("/**").authenticated()
////            .and()
////                .apply(new SpringSocialConfigurer());
//    }
//}
