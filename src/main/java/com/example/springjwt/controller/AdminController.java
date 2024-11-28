package com.example.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Admin 관련 요청을 처리하는 컨트롤러 클래스.
 * 관리자가 접근할 수 있는 엔드포인트를 정의.
 */
@Controller
@ResponseBody
public class AdminController {

    /**
     * "/admin" 경로로의 GET 요청을 처리.
     *
     * @return "admin Controller" 문자열을 응답.
     *         ResponseBody가 선언되어 있으므로, View를 반환하지 않고
     *         문자열 자체가 HTTP 응답으로 전송.
     */
    @GetMapping("/admin")
    public String adminP(){
        return "admin Controller";
    }
}
