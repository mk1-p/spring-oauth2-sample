package com.example.oauth2sample.global.security.config;


import com.example.oauth2sample.domain.model.Role;
import com.example.oauth2sample.global.security.application.AuthUserServiceImpl;
import com.example.oauth2sample.global.security.filter.AuthenticTokenFilter;
import com.example.oauth2sample.global.security.handler.OAuth2LoginFailureHandler;
import com.example.oauth2sample.global.security.handler.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AuthUserServiceImpl authUserServiceImpl;
    private final AuthenticTokenFilter authenticTokenFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize       // 권한 지정
                        .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/login/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).hasAuthority(Role.COMMON.getKey())
                )
                // 세션 사용 설정
                .sessionManagement(session -> session
                        // 세션을 사용하지 않겠다는 의미
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .formLogin(form -> form
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginPage("/user/login").permitAll()
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                )
                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/user/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(authUserServiceImpl))
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureUrl("/login?error=true")
                        .failureHandler(oAuth2LoginFailureHandler)
                );

        http.addFilterBefore(authenticTokenFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


}
