package com.roommake.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 입력폼
 */
@Getter
@Setter
public class UserSignupForm {
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;

}
