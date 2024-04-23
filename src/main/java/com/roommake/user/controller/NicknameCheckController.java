package com.roommake.user.controller;

import com.roommake.user.exception.AlreadyUsedNicknameException;
import com.roommake.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NicknameCheckController {

    private final UserService userService;

    /**
     * 닉네임 중복을 확인하는 엔드포인트
     *
     * @param nickname 닉네임
     * @return 중복 여부에 대한 ResponseEntity
     */
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
}


