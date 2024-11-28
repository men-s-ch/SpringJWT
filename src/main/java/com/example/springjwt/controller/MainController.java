package com.example.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 메인 페이지 요청을 처리하는 컨트롤러 클래스.
 */
@Controller
@ResponseBody
public class MainController {

    /**
     * "/" 경로로의 GET 요청을 처리.
     *
     * @return "Main Controller" 문자열을 HTTP 응답으로 전송.
     *         ResponseBody가 선언되어 있으므로 문자열이 View로 처리되지 않고
     *         HTTP 응답 본문으로 전송됨.
     */
    @GetMapping("/")
    public String mainP(){
        return "Main Controller";
    }
}
