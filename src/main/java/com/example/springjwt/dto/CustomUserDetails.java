package com.example.springjwt.dto;

import com.example.springjwt.entity.UserEntity; // UserEntity 클래스 (데이터베이스 사용자 엔티티)
import org.springframework.security.core.GrantedAuthority; // Spring Security에서 사용자의 권한을 나타내는 인터페이스
import org.springframework.security.core.userdetails.UserDetails; // Spring Security의 사용자 정보를 나타내는 인터페이스

import java.util.ArrayList; // ArrayList 클래스 (컬렉션 사용)
import java.util.Collection; // Collection 인터페이스 (권한 목록 반환 시 사용)

/**
 * Spring Security에서 사용자의 세부 정보를 정의하는 클래스.
 * UserEntity를 기반으로 UserDetails를 구현하여 인증 과정에서 사용.
 */
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity; // 사용자 정보를 저장하는 UserEntity 객체

    /**
     * CustomUserDetails 생성자
     *
     * @param userEntity UserEntity 객체
     */
    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    /**
     * 사용자의 권한 정보를 반환.
     *
     * @return GrantedAuthority의 컬렉션
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한(역할)을 담을 컬렉션 생성
        Collection<GrantedAuthority> collection = new ArrayList<>();

        // 권한을 컬렉션에 추가
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole(); // 데이터베이스에서 가져온 역할 반환
            }
        });

        return collection;
    }

    /**
     * 사용자의 비밀번호를 반환.
     *
     * @return 사용자 비밀번호
     */
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    /**
     * 사용자의 이름(아이디)을 반환.
     *
     * @return 사용자 이름(아이디)
     */
    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    /**
     * 계정이 만료되지 않았는지 확인.
     *
     * @return true (현재 모든 계정이 만료되지 않는다고 가정)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠기지 않았는지 확인.
     *
     * @return true (현재 모든 계정이 잠기지 않는다고 가정)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호(자격 증명)가 만료되지 않았는지 확인.
     *
     * @return true (현재 모든 자격 증명이 만료되지 않는다고 가정)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되었는지 확인.
     *
     * @return true (현재 모든 계정이 활성화되었다고 가정)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
