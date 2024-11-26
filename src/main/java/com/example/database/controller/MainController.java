package com.example.database.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * 메인 페이지를 반환
     * @param model 모델 객체로 데이터를 전달
     * @return HTML 템플릿 경로
     */
    @GetMapping("/")
    public String getMainPage(Model model) {
        // 메인 페이지에 필요한 데이터를 추가
        model.addAttribute("welcomeMessage", "Welcome to the Product Search Application!");
        return "index"; // src/main/resources/templates/index.html로 매핑
    }
}
