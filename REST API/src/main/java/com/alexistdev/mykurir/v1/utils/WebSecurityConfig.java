package com.alexistdev.mykurir.v1.utils;

import com.alexistdev.mykurir.v1.models.entity.Role;
import com.alexistdev.mykurir.v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http    .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/auth/register",
                                "/v1/api/auth/login").permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/users/get_all_users").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,
                                "/v1/api/region/*").hasAuthority(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET,
                                "/v1/api/region/*").hasAnyAuthority(Role.ADMIN.toString(),Role.USER.toString())

                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .authenticationProvider(this.daoAuthenticationProvider());
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(HttpMethod.POST,"/v1/api/auth/login")
                .requestMatchers(HttpMethod.POST,"/v1/api/auth/register")
                .requestMatchers(HttpMethod.GET,"/swagger-ui/index.html")
                .requestMatchers(HttpMethod.GET,"/v3/api-docs/**");
    }

    private DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
