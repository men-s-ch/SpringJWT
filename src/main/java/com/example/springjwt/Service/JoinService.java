package com.example.springjwt.Service;

import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * JoinService는 회원가입 로직을 처리하는 서비스 클래스입니다.
 */
@Service
public class JoinService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder; // 비밀번호 암호화를 위한 객체.
    private final UserRepository userRepository; // 사용자 데이터를 처리하는 리포지토리.

    /**
     * 생성자 주입 방식으로 UserRepository와 BCryptPasswordEncoder 주입.
     *
     * @param userRepository 사용자 데이터 처리를 위한 리포지토리 객체.
     * @param bCryptPasswordEncoder 비밀번호 암호화를 위한 인코더 객체.
     */
    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 회원가입 프로세스를 처리합니다.
     *
     * @param userDTO 회원가입 요청 데이터 (DTO).
     */
    public void joinProcess(UserDTO userDTO) {
        String username = userDTO.getUsername(); // DTO에서 사용자 이름 가져오기.
        String password = userDTO.getPassword(); // DTO에서 비밀번호 가져오기.

        // 사용자 이름 중복 여부 확인.
        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            // 사용자 이름이 이미 존재하면 가입을 진행하지 않고 종료.
            return;
        }

        // 새 사용자 데이터 생성.
        UserEntity data = new UserEntity();
        data.setUsername(username); // 사용자 이름 설정.
        data.setPassword(bCryptPasswordEncoder.encode(password)); // 비밀번호 암호화 후 설정.
        data.setRole("ROLE_ADMIN"); // 기본 역할을 ADMIN으로 설정.

        // 사용자 데이터를 데이터베이스에 저장.
        userRepository.save(data);
    }
}
