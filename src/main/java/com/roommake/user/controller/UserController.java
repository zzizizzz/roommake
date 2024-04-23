package com.roommake.user.controller;

import com.roommake.user.dto.UserSignupForm;
import com.roommake.user.exception.AlreadyUsedEmailException;
import com.roommake.user.service.UserService;
import com.roommake.user.vo.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 로그인
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "/user/loginform";
    }

    /**
     * 회원가입 폼 (조회)
     *
     * @param model
     * @return
     */
    @GetMapping("/signup")
    public String form(Model model) {
        model.addAttribute("userSignupForm", new UserSignupForm());
        return "/user/signupform";
    }

    /**
     * 회원가입 (등록)
     *
     * @param form
     * @param errors
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/signup")
    public String signup(@Valid UserSignupForm form, BindingResult errors, RedirectAttributes redirectAttributes) {

        // 폼 입력값 유효성 체크를 통과하지 못한 경우, 회원가입화면으로 내부이동
        if (errors.hasErrors()) {
            return "/user/signupform";
        }
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "/user/signupform";
        }

        if (!userService.isNicknameUnique(form.getNickname())) {
            errors.rejectValue("nickname", "error.nickname", "이미 사용중인 닉네임 입니다.");
            return "/user/signupform";
        }
        try {
            // 폼 입력값 유효성 체크를 통과한 경우
            User user = userService.createUser(form);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/user/login";
        } catch (AlreadyUsedEmailException ex) {
            errors.rejectValue("email1", null, ex.getMessage());
            return "/user/signupform";
        }
    }

    /**
     * 비밀번호 변경 - 이메일 인증
     *
     * @return
     */
    @GetMapping("/resetpwd1")
    public String resetpwd1() {
        return "user/reset-password";
    }

    /**
     * 비밀번호 변경 폼
     *
     * @return
     */
    @GetMapping("/resetpwd2")
    public String resetpwd2() {
        return "user/reset-passwordform";
    }

    /**
     * 마이페이지 메인
     *
     * @return
     */
    @GetMapping("/mypage")
    public String mypage1() {
        return "user/mypage-main";
    }

    /**
     * 마이페이지 - 커뮤니티
     *
     * @return
     */
    @GetMapping("/mycomm")
    public String mypage2() {
        return "user/mypage-community";
    }

    /**
     * 마이페이지-스크랩북(모두)
     *
     * @return
     */
    @GetMapping("/scrapbook")
    public String scrapbook() {
        return "user/mypage-scrapbook";
    }

    /**
     * 마이페이지 - 스크랩북(폴더)
     *
     * @return
     */
    @GetMapping("/scrapbook1")
    public String scrapbook1() {
        return "user/mypage-scrapbook1";
    }

    /**
     * 마이페이지 - 스크랩북(상품)
     *
     * @return
     */
    @GetMapping("/scrapbook2")
    public String scrapbook2() {
        return "user/mypage-scrapbook2";
    }

    /**
     * 마이페이지 - 스크랩북(커뮤니티)
     *
     * @return
     */
    @GetMapping("/scrapbook3")
    public String scrapbook3() {
        return "user/mypage-scrapbook3";
    }

    /**
     * 마이페이지 - 좋아요
     *
     * @return
     */
    @GetMapping("/heart")
    public String heart() {

        return "user/mypage-heart";
    }

    /**
     * 마이페이지 - 주문배송목록
     *
     * @return
     */
    @GetMapping("/myorder")
    public String myorder() {

        return "user/mypage-order";
    }

    /**
     * 마이페이지 - 나의문의내역
     *
     * @return
     */
    @GetMapping("/myqna")
    public String myqna() {

        return "user/mypage-qna";
    }

    /**
     * 마이페이지 - 포인트
     *
     * @return
     */
    @GetMapping("/point")
    public String point() {

        return "user/mypage-point";
    }
}