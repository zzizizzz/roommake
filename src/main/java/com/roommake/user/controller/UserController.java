package com.roommake.user.controller;

import com.roommake.user.dto.UserSignupForm;
import com.roommake.user.exception.AlreadyUsedEmailException;
import com.roommake.user.exception.AlreadyUsedNicknameException;
import com.roommake.user.service.UserService;
import com.roommake.user.vo.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "로그인, 회원가입, 마이페이지 API를 제공한다.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인 폼", description = "로그인 폼을 조회한다.")
    @GetMapping("/login")
    public String loginform(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("fail".equals(error)) {
            model.addAttribute("loginError", "이메일 또는 비밀번호를 확인해 주세요.");
        }
        return "/user/loginform"; // 로그인 페이지의 뷰 이름
    }

    @Operation(summary = "회원가입 폼", description = "회원가입 폼을 조회한다.")
    @GetMapping("/signup")
    public String sinupform(Model model) {
        model.addAttribute("userSignupForm", new UserSignupForm());
        return "/user/signupform";
    }

    @Operation(summary = "회원가입 등록", description = "신규 회원을 등록한다.")
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

    @Operation(summary = "닉네임 중복확인", description = "닉네임 중복을 확인하는 엔드포인트")
    @ResponseBody
    @PostMapping("/check-duplicate-nickname")
    public ResponseEntity<?> checkDuplicateNickname(@RequestBody String nickname) {
        try {
            boolean isDuplicate = userService.isNicknameUnique(nickname);
            if (isDuplicate) {
                // 닉네임이 중복된 경우
                return ResponseEntity.ok(false);
            } else {
                // 닉네임이 중복되지 않은 경우
                return ResponseEntity.ok(true);
            }
        } catch (AlreadyUsedNicknameException e) {
            // 이미 사용된 닉네임인 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 닉네임입니다.");
        } catch (Exception e) {
            // 그 외 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("닉네임 중복 검사 중 오류가 발생했습니다.");
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

    @Operation(summary = "인증인가 예외처리", description = "인증,인가 과정에서 문제발생시 오류페이지로 이동한다.")
    @GetMapping("/accessdenied")
    public String accessDenied() {
        return "/user/access-denied";
    }
}