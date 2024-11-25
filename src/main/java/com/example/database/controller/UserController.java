package com.example.database.controller;


import com.example.database.service.UserService;
import com.example.database.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/register")
    public String registerUser(User user) {
        userService.registerUser(user);
        return "redirect:/users/login";
    }

    // 이메일로 사용자 조회
    @GetMapping("/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // 로그인 상태라면 마이페이지로 리다이렉트
            return "redirect:/users/mypage";
        }
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {

        boolean isAuthenticated = userService.authenticateUser(email, password);

        if (isAuthenticated) {
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                session.setAttribute("loggedInUser", user);  // 세션에 User 객체 저장
                System.out.println("로그인 성공: " + user);
            }

            return "redirect:/main"; // 메인 페이지로 리다이렉트
        } else {
            return "redirect:/users/login?error=true"; // 실패 시 로그인 페이지로 돌아가기
        }
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/users/login"; // 로그인 페이지로 리다이렉트
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String RegisterPage() {
        return "register";
    }
    // 마이페이지 (로그인한 사용자의 정보를 표시)
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";  // 로그인 상태가 아니면 로그인 페이지로 리다이렉트
        }
        model.addAttribute("loggedInUser", loggedInUser);  // 모델에 User 객체 추가
        return "mypage";  // mypage.html 페이지로 이동
    }
    // 비밀번호 변경 페이지
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }
    // 비밀번호 변경 처리
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session) {
        String loggedInUserEmail = (String) session.getAttribute("loggedInUser");

        if (loggedInUserEmail == null) {
            return "redirect:/users/login";  // 로그인 상태가 아니면 로그인 페이지로 리다이렉트
        }

        // 비밀번호 확인
        Optional<User> optionalUser = userService.findByEmail(loggedInUserEmail);
        if (!optionalUser.isPresent()) {
            return "redirect:/users/login";  // 사용자 없으면 로그인 페이지로
        }

        User user = optionalUser.get();

        // 현재 비밀번호와 일치하는지 확인
        if (!userService.authenticateUser(user.getEmail(), currentPassword)) {
            return "redirect:/users/change-password?error=true";  // 현재 비밀번호가 맞지 않으면 에러 페이지로 리다이렉트
        }

        // 새 비밀번호 확인
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/users/change-password?error=true";  // 새 비밀번호와 확인 비밀번호가 다르면 에러 페이지로
        }

        // 비밀번호 변경
        user.setPassword(newPassword);  // 새 비밀번호로 변경
        userService.updateUser(user);  // 사용자 정보 업데이트

        return "redirect:/users/mypage";  // 마이페이지로 리다이렉트
    }
    // 회원 탈퇴 처리
    @PostMapping("/delete-account")
    public String deleteAccount(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            userService.deleteUser(loggedInUser.getId());  // 사용자 삭제
            session.invalidate();  // 세션 무효화 (로그아웃 처리)
        }

        return "redirect:/users/login";  // 로그인 페이지로 리다이렉트
    }
}
