package com.roommake.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Term {

    private int id;           // 이용약관 번호
    private String title;     // 이용약관 제목
    private String content;   // 이용약관 내용
    private Date createDate;  // 이용약관 생성일
    private String requireYn; // 필수여부
}
