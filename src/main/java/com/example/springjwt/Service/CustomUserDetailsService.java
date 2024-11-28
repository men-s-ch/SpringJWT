package com.example.springjwt.Service;

import com.example.springjwt.dto.CustomUserDetails; // 사용자 정의 UserDetails 클래스
import com.example.springjwt.entity.UserEntity; // 데이터베이스 사용자 엔티티 클래스
import com.example.springjwt.repository.UserRepository; // 사용자 데이터베이스 접근 레포지토리
import org.springframework.security.core.userdetails.UserDetails; // Spring Security에서 사용자의 인증 정보를 담는 인터페이스
import org.springframework.security.core.userdetails.UserDetailsService; // Spring Security에서 사용자 정보를 불러오는 인터페이스
import org.springframework.security.core.userdetails.UsernameNotFoundException; // 사용자 이름이 없을 때 던지는 예외
import org.springframework.stereotype.Service; // Spring 서비스 레이어를 나타냄

@Service // Spring에서 서비스 레이어의 Bean으로 등록
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 사용자 정보를 DB에서 가져오기 위한 레포지토리

    /**
     * 생성자 주입을 통해 UserRepository를 의존성 주입
     *
     * @param userRepository 사용자 데이터베이스 접근 레포지토리
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 이름으로 사용자 정보를 로드
     *
     * @param username 사용자 이름
     * @return Spring Security의 UserDetails 객체
     * @throws UsernameNotFoundException 사용자 이름이 데이터베이스에 없을 때 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 데이터베이스에서 사용자 이름에 해당하는 사용자 정보 검색
        UserEntity userData = userRepository.findByUsername(username);

        // 사용자가 존재하면 CustomUserDetails 객체로 변환하여 반환
        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        // 사용자가 존재하지 않으면 null 반환 (Spring Security에서는 null 대신 예외 처리 권장)
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
