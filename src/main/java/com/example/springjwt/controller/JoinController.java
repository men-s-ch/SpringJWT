package com.example.springjwt.controller;

import com.example.springjwt.Service.JoinService;
import com.example.springjwt.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 회원가입 요청을 처리하는 컨트롤러 클래스.
 */
@Controller
@ResponseBody
public class JoinController {

    // JoinService를 주입받아 회원가입 로직을 처리.
    private final JoinService joinService;

    /**
     * 생성자 주입을 통해 JoinService 의존성을 주입.
     * @param joinService 회원가입 처리를 위한 서비스 객체.
     */
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    /**
     * "/join" 경로로의 POST 요청을 처리.
     * 클라이언트에서 전달받은 사용자 정보를 이용해 회원가입 진행.
     *
     * @param userDTO 회원가입 요청 데이터 (DTO 객체로 전달).
     * @return "ok" 문자열을 HTTP 응답으로 전송.
     */
    @PostMapping("/join")
    public String joinProcess(UserDTO userDTO) {
        // JoinService를 사용하여 회원가입 로직 실행.
        joinService.joinProcess(userDTO);

        // 성공적으로 처리된 경우 "ok" 반환.
        return "ok";
    }
}
