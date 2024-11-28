package com.example.springjwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * UserEntity 클래스는 데이터베이스의 사용자 정보를 나타내는 엔티티 클래스입니다.
 * JPA를 사용하여 테이블과 매핑됩니다.
 */
@Entity
@Getter
@Setter
public class UserEntity {

    /**
     * 기본 키로 사용되는 id 필드.
     * @Id: 해당 필드가 Primary Key임을 명시.
     * @GeneratedValue: id 값을 자동 생성하도록 설정.
     * GenerationType.IDENTITY: 데이터베이스의 기본 키 생성 전략을 따름 (주로 Auto Increment 사용).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 사용자의 이름(username).
     * 해당 필드는 데이터베이스에서 username 열로 매핑됩니다.
     */
    private String username;

    /**
     * 사용자의 암호(password).
     * 저장 시 암호화된 값으로 저장되어야 합니다.
     */
    private String password;

    /**
     * 사용자의 역할(role).
     * 예: "ROLE_USER", "ROLE_ADMIN" 등.
     */
    private String role;

}
