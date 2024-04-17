package com.roommake.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TermAgreement {

    private Term termId;        // 이용약관 번호
    private User userId;        // 유저 번호
    private Date createDate;    // 약관동의 생성일
    private Date updateDate;    // 약관동의 수정일
}
