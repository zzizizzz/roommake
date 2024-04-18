package com.roommake.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 입력폼
 */
@Getter
@Setter
public class UserSignupForm {
    
    private String email;               // 유저 이메일
    private String password;            // 유저 비밀번호
    private String confirmPassword;     // 유저 비밀번호 확인
    private String nickname;            // 유저 닉네임
    private String optionRecommendCode; // 추천코드 (가입시 선택 입력)
}
