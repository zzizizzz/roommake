package com.roommake.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    /**
     * 로그인
     * @return
     */
    @GetMapping("/login")
    public String login () {
        return "user/loginform";
    }

    /**
     * 회원가입
     * @param model
     * @return
     */
    @GetMapping("/signup")
    public String signup (Model model) {
        model.addAttribute("userSignupForm", new UserSignupForm());
        return "user/signupform";
    }

    /**
     * 비밀번호 변경 - 이메일 인증
     * @return
     */
    @GetMapping("/resetpwd1")
    public String resetpwd1 () {
        return "user/reset-password";
    }

    /**
     * 비밀번호 변경 폼
     * @return
     */
    @GetMapping("/resetpwd2")
    public String resetpwd2 () {
        return "user/reset-passwordform";
    }

}
