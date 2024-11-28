package com.example.springjwt.jwt;

import com.example.springjwt.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 커스텀 로그인 필터 클래스.
 * 사용자의 인증 요청을 처리하며, Spring Security의 `UsernamePasswordAuthenticationFilter`를 확장.
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;


    /**
     * 생성자를 통해 AuthenticationManager 주입.
     *
     * @param authenticationManager 인증 처리를 담당하는 AuthenticationManager
     */
    public LoginFilter(AuthenticationManager authenticationManager , JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 인증 시도 메서드.
     * 사용자 요청에서 인증 정보를 추출하고, 인증 토큰을 생성한 뒤 인증을 시도합니다.
     *
     * @param request  클라이언트 요청 객체
     * @param response 클라이언트 응답 객체
     * @return 인증 결과를 반환 (Authentication 객체)
     * @throws AuthenticationException 인증 실패 시 예외 발생
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 요청에서 username과 password 추출
        String username = obtainUsername(request); // 기본적으로 "username" 키 값으로 데이터 추출
        String password = obtainPassword(request); // 기본적으로 "password" 키 값으로 데이터 추출

        // 디버깅용 출력 (추후 로그로 변경 권장)
        System.out.println("Attempting authentication for username: " + username);

        // UsernamePasswordAuthenticationToken 생성 (권한 정보는 null로 설정)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // AuthenticationManager를 통해 인증 처리
        return authenticationManager.authenticate(authToken);
    }

    /**
     * 인증 성공 시 호출되는 메서드.
     * JWT 토큰 생성 및 응답에 추가 등 성공 후 작업 처리.
     *
     * @param request        클라이언트 요청 객체
     * @param response       클라이언트 응답 객체
     * @param chain          필터 체인
     * @param authentication 인증 정보 객체
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);
    }

    /**
     * 인증 실패 시 호출되는 메서드.
     * 실패 원인에 따라 적절한 응답을 작성.
     *
     * @param request  클라이언트 요청 객체
     * @param response 클라이언트 응답 객체
     * @param failed   인증 예외 객체
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 인증 실패 시 처리
       response.setStatus(401);
    }
}
