package com.eight.sailingship.config;

import com.eight.sailingship.auth.exception.JwtAccessDenyHandler;
import com.eight.sailingship.auth.exception.JwtAuthenticationEntryPoint;
import com.eight.sailingship.auth.jwt.JwtAuthenticationFilter;
import com.eight.sailingship.auth.jwt.JwtAuthorizationFilter;
import com.eight.sailingship.auth.jwt.JwtUtil;
import com.eight.sailingship.auth.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    // 필터단 예외
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 예외 커스텀 메시지 던지기
    private final JwtAccessDenyHandler jwtAccessDenyHandler; // 인가 예외 커스텀 메시지 던지기(역할별 접근권한같은)

    // 인증 매니저 생성
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception { // 인증필터 생성
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration)); // 인증매니저 설정
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() { // 인가필터 생성
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
//                        .requestMatchers("swagger-ui/**", "/v3/**").permitAll() // Swagger 관련
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger UI 접근 허용
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/sail/store/{storeId}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/sail/signup").permitAll()
                                .requestMatchers(HttpMethod.POST, "/sail/signup").permitAll()
                                .requestMatchers("/sail/login").permitAll()
                                .requestMatchers("/sail/user/login").permitAll()
                                .requestMatchers("/sail/user/email-verify").permitAll()
                                .requestMatchers("/sail/user/email-check").permitAll()
                                .requestMatchers("/sail/authInfo").permitAll()
                                .requestMatchers("/sail/image").permitAll() // 잠시 이미지 구현을 위해 permitall****
//                                .requestMatchers("/api/products/{productId}").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN") // 1차 권한방어
//                                .requestMatchers(HttpMethod.POST, "/api/images").hasRole("SUPER_ADMIN")
                                .anyRequest().authenticated() // 그 외 모든 요청 인증처리 요구
        );

        http.exceptionHandling(e ->
                e.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDenyHandler));

        // 로그인 사용
        http.formLogin((formLogin) ->
                        formLogin
                                // 로그인 View 제공 (GET /api/user/login-page)
                                .loginPage("/sail/login")
                                // 로그인 처리 (POST /api/user/login)
                                .loginProcessingUrl("/sail/user/login") // 둘을 똑같이 작성하면 안 됨
                                // 로그인 처리 후 성공 시 URL
                                .defaultSuccessUrl("/")
                                // 로그인 처리 후 실패 시 URL
                                .failureUrl("/")
                                .permitAll()
        );

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class); // 인가 처리 필터
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // 인증(+ 로그인) 처리 필터

        return http.build();
    }
}

//package com.sparta.springauth.config;
//
//import com.sparta.springauth.jwt.JwtAuthorizationFilter;
//import com.sparta.springauth.jwt.JwtAuthenticationFilter;
//import com.sparta.springauth.jwt.JwtUtil;
//import com.sparta.springauth.security.UserDetailsServiceImpl;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity // Spring Security 지원을 가능하게 함
//public class WebSecurityConfig {
//
//    private final JwtUtil jwtUtil;
//    private final UserDetailsServiceImpl userDetailsService;
//    private final AuthenticationConfiguration authenticationConfiguration;
//
//    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationConfiguration authenticationConfiguration) {
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//        this.authenticationConfiguration = authenticationConfiguration;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
//        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
//        return filter;
//    }
//
//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() {
//        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // CSRF 설정
//        http.csrf((csrf) -> csrf.disable());
//
//        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
//        http.sessionManagement((sessionManagement) ->
//                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        );
//
//        http.authorizeHttpRequests((authorizeHttpRequests) ->
//                authorizeHttpRequests
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
//                        .requestMatchers("/api/user/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
//                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
//        );
//
//        http.formLogin((formLogin) ->
//                formLogin
//                        .loginPage("/api/user/login-page").permitAll()
//        );
//
//        // 필터 관리
//        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}