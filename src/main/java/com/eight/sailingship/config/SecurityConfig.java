package com.eight.sailingship.config;

import com.eight.sailingship.service.customer.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) -> auth.disable()); //배포할때 다시설정


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/sail/login", "/sail/signup").permitAll()
                        .requestMatchers("/admin").hasRole("OWNER")
                        .requestMatchers("/my/**").hasAnyRole("OWNER", "CUSTOMER")
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/sail/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

//        http
//                .sessionManagement((auth) -> auth
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(false)
//                )
//                .sessionFixation().none();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
