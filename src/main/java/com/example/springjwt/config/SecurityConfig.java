package com.example.springjwt.config;

import com.example.springjwt.jwt.JWTUtil;
import com.example.springjwt.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스.
 * 애플리케이션의 보안 정책을 정의하며, CSRF, 인증 방식, 권한 설정 등을 포함합니다.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    /**
     * AuthenticationConfiguration을 생성자 주입.
     * @param authenticationConfiguration 인증 관련 설정 객체
     */
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    /**
     * AuthenticationManager를 Bean으로 등록.
     * AuthenticationManager는 인증 요청을 처리하는 핵심 컴포넌트입니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 비밀번호를 암호화하기 위한 BCryptPasswordEncoder를 Bean으로 등록.
     * BCrypt는 강력한 해싱 알고리즘을 사용하여 암호화 보안을 강화합니다.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security의 필터 체인을 정의.
     * 애플리케이션의 인증, 권한 정책, 보안 필터 등을 설정합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF 보호 비활성화 (REST API 서버는 보통 CSRF 보호를 사용하지 않음)
        http.csrf(csrf -> csrf.disable());

        // 폼 기반 로그인 비활성화 (JWT와 같은 커스텀 인증 방식 사용 시 필요)
        http.formLogin(form -> form.disable());

        // HTTP Basic 인증 방식 비활성화 (JWT와 같은 헤더 기반 인증을 사용하는 경우)
        http.httpBasic(basic -> basic.disable());

        // 요청 경로별 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/", "/join").permitAll() // 로그인, 메인, 회원가입 페이지는 모두 접근 가능
                .requestMatchers("/admin").hasRole("ADMIN") // /admin 경로는 ADMIN 역할을 가진 사용자만 접근 가능
                .anyRequest().authenticated() // 나머지 모든 요청은 인증된 사용자만 접근 가능
        );

        // 커스텀 로그인 필터 추가 (UsernamePasswordAuthenticationFilter 앞에 위치)
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        // 세션 정책 설정: STATELESS (서버가 세션을 생성하지 않음)
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 설정된 SecurityFilterChain을 반환
        return http.build();
    }
}
