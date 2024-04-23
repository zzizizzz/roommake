package com.roommake.user.controller;

import com.roommake.user.exception.EmailException;
import com.roommake.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이메일 인증을 위한 REST 컨트롤러
 */
@RestController
public class EmailVerifyController {

    @Autowired
    private UserService userService;

    /**
     * 이메일로 인증 코드를 발송하는 엔드포인트
     *
     * @param email 인증 코드를 받을 이메일 주소
     * @return ResponseEntity 발송 성공 메시지를 반환
     */
    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerifyCode(@RequestParam String email) {
        try {
            userService.sendVerifyCode(email);
            return ResponseEntity.ok("입력한 이메일로 인증 메일이 발송되었습니다.\n이메일에 표시된 인증코드를 입력해주세요.");
        } catch (EmailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 발송 중 오류가 발생했습니다.");
        }
    }

    /**
     * 제공된 인증 코드를 검증하는 엔드포인트
     *
     * @param code  사용자로부터 받은 인증 코드
     * @param email 인증 코드가 발송된 이메일 주소
     * @return ResponseEntity 코드의 유효성 검사 결과
     */
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String code, @RequestParam String email) {
        boolean isValidCode = userService.verifyEmail(email, code);
        if (isValidCode) {
            // 인증 코드가 유효한 경우
            return ResponseEntity.ok().body("{\"valid\": true}");
        } else {
            // 인증 코드가 유효하지 않은 경우
            return ResponseEntity.badRequest().body("{\"valid\": false}");
        }
    }

    /**
     * 이메일 서비스에서 발생하는 모든 예외를 처리하는 메소드
     *
     * @param ex 발생한 예외
     * @return ResponseEntity 에러 메시지와 함께 상태 코드 반환
     */
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<String> handleEmailServiceException(EmailException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인증 코드 발송에 실패하였습니다.");
    }
}
