package com.example.springjwt.repository;

import com.example.springjwt.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserEntity와 데이터베이스 간의 CRUD 작업을 수행하는 리포지토리 인터페이스.
 * JpaRepository를 상속하여 기본적인 CRUD 메서드를 제공합니다.
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * 특정 사용자 이름(username)이 데이터베이스에 존재하는지 확인하는 메서드.
     * Spring Data JPA가 메서드 이름을 분석하여 적절한 쿼리를 생성합니다.
     *
     * @param username 확인할 사용자 이름.
     * @return 주어진 username이 존재하면 true, 그렇지 않으면 false.
     */
    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}
