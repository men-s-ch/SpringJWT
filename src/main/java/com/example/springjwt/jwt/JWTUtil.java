package com.example.springjwt.jwt;

import io.jsonwebtoken.Jwts; // JWT 생성을 위한 라이브러리
import org.springframework.beans.factory.annotation.Value; // 환경 변수 값 주입
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // JWT 서명에 사용되는 키
import javax.crypto.spec.SecretKeySpec; // SecretKey 생성에 사용
import java.nio.charset.StandardCharsets; // 문자열 -> 바이트 변환을 위한 표준 캐릭터셋
import java.util.Date; // 날짜 처리

@Component // Spring에서 관리되는 Bean으로 등록
public class JWTUtil {

    private SecretKey secretKey; // JWT 서명에 사용할 SecretKey

    /**
     * 생성자에서 secretKey를 초기화
     *
     * @param secret 외부에서 주입받은 비밀 키 값
     * SecretKeySpec: 주어진 바이트 배열과 알고리즘으로 SecretKey 생성
     * Jwts.SIG.HS256.key().build(): HS256 알고리즘을 사용하도록 설정
     */
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * JWT에서 username을 추출
     *
     * @param token JWT 문자열
     * @return 추출된 사용자 이름
     * verifyWith(secretKey): secretKey를 사용하여 서명 검증
     * parseSignedClaims: JWT의 서명과 payload를 검증 및 파싱
     * getPayload(): payload 데이터에 접근
     * get("username", String.class): "username" 필드의 값을 String으로 반환
     */
    public String getUsername(String token) {
        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .get("username", String.class);
    }

    /**
     * JWT에서 role을 추출
     *
     * @param token JWT 문자열
     * @return 추출된 사용자 역할
     * get("role", String.class): "role" 필드의 값을 String으로 반환
     */
    public String getRole(String token) {
        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .get("role", String.class);
    }

    /**
     * JWT의 만료 여부 확인
     *
     * @param token JWT 문자열
     * @return 만료 여부 (true: 만료됨, false: 유효)
     * getExpiration(): payload에서 만료 날짜를 가져옴
     * before(new Date()): 만료 날짜가 현재 시간보다 이전인지 확인
     */
    public Boolean isExpired(String token) {
        return Jwts.parser()
                   .verifyWith(secretKey)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload()
                   .getExpiration()
                   .before(new Date());
    }

    /**
     * 새로운 JWT 생성
     *
     * @param username 사용자 이름
     * @param role 사용자 역할
     * @param expiredMs 만료 시간 (밀리초 단위)
     * @return 생성된 JWT
     * claim(): payload에 데이터 추가
     * issuedAt(new Date()): JWT 발급 시간 추가
     * signWith(secretKey): SecretKey를 사용하여 서명
     * compact(): 최종적으로 JWT를 문자열로 반환
     */
    public String createJwt(String username, String role, Long expiredMs) {
        return Jwts.builder()
                   .claim("username", username)
                   .claim("role", role)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .signWith(secretKey)
                   .compact();
    }
}
