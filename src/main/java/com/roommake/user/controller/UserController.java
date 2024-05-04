package com.roommake.user.controller;

import com.roommake.community.dto.MyPageCommunity;
import com.roommake.community.service.CommunityService;
import com.roommake.resolver.Login;
import com.roommake.user.dto.UserSettingForm;
import com.roommake.user.dto.UserSignupForm;
import com.roommake.user.emuns.UserStatusEnum;
import com.roommake.user.exception.AlreadyUsedEmailException;
import com.roommake.user.exception.AlreadyUsedNicknameException;
import com.roommake.user.security.LoginUser;
import com.roommake.user.service.UserService;
import com.roommake.user.vo.Term;
import com.roommake.user.vo.TermAgreement;
import com.roommake.user.vo.User;
import com.roommake.utils.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "로그인, 회원가입, 마이페이지 API를 제공한다.")
public class UserController {

    private final UserService userService;
    private final CommunityService communityService;
    private final S3Uploader s3Uploader;

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
        List<Term> terms = userService.getAllTerms();
        model.addAttribute("userSignupForm", new UserSignupForm());
        model.addAttribute("terms", terms);
        return "/user/signupform";
    }

    @Operation(summary = "회원가입 등록", description = "신규 회원을 등록한다.")
    @PostMapping("/signup")
    @Transactional
    public String signup(@Valid UserSignupForm form, BindingResult errors, RedirectAttributes redirectAttributes) {
        System.out.println("회원가입 절차 시작");

        if (errors.hasErrors()) {
            System.out.println("폼 검증 오류 발생: " + errors);
            return "/user/signupform";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            System.out.println("비밀번호 확인 불일치");
            errors.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "/user/signupform";
        }

        if (!userService.isNicknameUnique(form.getNickname())) {
            System.out.println("닉네임 중복 발견: " + form.getNickname());
            errors.rejectValue("nickname", "error.nickname", "이미 사용중인 닉네임입니다.");
            return "/user/signupform";
        }

        // 추천인 코드 입력 검증
        if (form.getOptionRecommendCode() != null && !form.getOptionRecommendCode().isEmpty()) {
            if (!userService.checkRecommendCodeExists(form.getOptionRecommendCode())) {
                System.out.println("추천인 코드 불일치: " + form.getOptionRecommendCode());
                errors.rejectValue("optionRecommendCode", "error.optionRecommendCode", "존재하지 않는 추천인 코드입니다.");
                return "/user/signupform";
            }
        }

        TermAgreement termAgreement = new TermAgreement();
        termAgreement.setAgree1(form.getTermAgreements1());
        termAgreement.setAgree2(form.getTermAgreements2());
        termAgreement.setAgree3(form.getTermAgreements3());

        if (form.getTermAgreements1() == null || form.getTermAgreements2() == null) {
            System.out.println("필수 약관에 모두 동의하지 않음");
            errors.reject("termAgreement", "모든 필수 약관에 동의해야 합니다.");
            return "/user/signupform";
        }

        try {
            User user = userService.createUser(form);
            System.out.println("사용자 생성 성공: User ID " + user.getId());
            // 약관 동의 정보 저장
            termAgreement.setUser(user);
            userService.agreeToTerms(termAgreement);

            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/user/login";
        } catch (AlreadyUsedEmailException ex) {
            System.out.println("사용중인 이메일로 인한 회원가입 실패: " + ex.getMessage());
            errors.rejectValue("email", null, ex.getMessage());
            return "/user/signupform";
        } catch (Exception ex) {
            System.out.println("회원가입 중 예외 발생: " + ex.getMessage());
            ex.printStackTrace();
            return "/user/default-error";
        }
    }

    @Operation(summary = "닉네임 중복확인", description = "닉네임 중복을 확인하는 엔드포인트")
    @ResponseBody
    @PostMapping("/check-duplicate-nickname")
    public ResponseEntity<?> checkDuplicateNickname(@RequestParam String nickname) {
        try {
            boolean isDuplicate = userService.isNicknameUnique(nickname);
            return ResponseEntity.ok(isDuplicate);
        } catch (AlreadyUsedNicknameException e) {
            // 이미 사용된 닉네임인 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 닉네임입니다.");
        } catch (Exception e) {
            // 그 외 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("닉네임 중복 검사 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "약관 조회", description = "약관을 조회한다.")
    @GetMapping("/terms/{id}")
    public String viewTerm(@PathVariable int id, Model model) {
        Term term = userService.getTermById(id);
        List<String> contentLines = Arrays.asList(term.getContent().split("\\r?\\n"));
        boolean required = term.getRequireYn().equals("Y");
        model.addAttribute("term", term);
        model.addAttribute("contentLines", contentLines);
        model.addAttribute("required", required);

        switch (id) {
            case 1:
                return "user/term1";
            case 2:
                return "user/term2";
            case 3:
                return "user/term3";
            default:
                return "user/default-error";
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

    @Operation(summary = "마이페이지 메인", description = "마이페이지 메인을 조회한다.")
    @GetMapping("/mypage")
    @PreAuthorize("isAuthenticated()")
    public String myPage(@Login LoginUser loginUser, Model model) {
        int userId = loginUser.getId();

        // 사용자가 등록한 게시물 목록을 조회
        List<MyPageCommunity> communities = communityService.getCommunitiesByUserId(userId);
        model.addAttribute("communities", communities);

        // 사용자가 작성한 커뮤니티 게시글의 총 개수를 조회
        int communityCount = communityService.countCommunitiesByUserId(userId);
        model.addAttribute("communityCount", communityCount);

        // 사용자가 작성한 댓글의 총 개수를 조회
        int replyCount = communityService.countRepliesByUserId(userId);
        model.addAttribute("replyCount", replyCount);

        // 마이페이지의 뷰 반환
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

    /**
     * 마이페이지 - 설정 조회
     *
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/setting")
    public String settingForm(Model model, Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        UserSettingForm userSettingForm = new UserSettingForm();

        userSettingForm.setEmail(email);
        userSettingForm.setNickname(user.getNickname());
        userSettingForm.setProfilePhotoUrl(user.getProfilePhoto() != null ? user.getProfilePhoto() : "/images/mypage/default.jpg");

        model.addAttribute("userSettingForm", userSettingForm);
        return "user/settingform";
    }

    @PostMapping("/setting")
    public String updateSettings(@ModelAttribute UserSettingForm form,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        if (principal == null) {
            return "redirect:/user/login"; // 로그인하지 않은 사용자를 로그인 페이지로 리다이렉션
        }
        try {
            // 사용자 설정 업데이트 서비스 호출
            userService.modifyUserSettings(form, principal.getName());

            // 성공 메시지를 리다이렉션 속성에 추가
            redirectAttributes.addFlashAttribute("successMessage", "회원 정보가 성공적으로 업데이트되었습니다.");
            return "redirect:/user/mypage"; // 성공 시 마이페이지 메인
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/user/setting"; // 실패 시 회원정보 수정 페이지
        }
    }

    @Operation(summary = "인증인가 예외처리", description = "인증,인가 과정에서 문제발생시 오류페이지로 이동한다.")
    @GetMapping("/accessdenied")
    public String accessDenied() {
        return "/user/access-denied";
    }

    @Operation(summary = "이메일 중복 검사", description = "입력된 이메일이 기존에 등록된 이메일인지 검사한다.")
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        boolean isExist = userService.isEmailAvailable(email);
        return ResponseEntity.ok(Collections.singletonMap("exists", isExist));
    }

    @Operation(summary = "회원 탈퇴 처리", description = "회원 탈퇴 처리한다.")
    @PostMapping("/withdraw")
    public String withdrawUser(Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);

        user.setStatus(UserStatusEnum.DELETE.getStatus());
        user.setPoint(0);

        userService.modifyUser(user);

        return "redirect:/user/logout";
    }
}