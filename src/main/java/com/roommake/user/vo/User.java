package com.roommake.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class User {

    private int id;               // 유저 번호
    private String email;         // 유저 이메일(아이디)
    private String password;      // 유저 비밀번호
    private String nickname;      // 유저 닉네임
    private String profilePhoto;  // 유저 프로필사진
    private String tel;           // 유저 연락처
    private Date birthDate;       // 유저 생일
    private String introduction;  // 유저 소개
    private String sns;           // 유저 SNS
    private Date createDate;      // 유저 가입일
    private Date updateDate;      // 유저 수정일
    private String recommendCode; // 유저 추천코드
    private String socialYn;      // 유저 소셜 로그인 여부
    private String status;        // 유저 상태
    private UserGrade gradeId;    // 등급번호
    private int point;            // 유저 보유포인트
    private String address;       // 유저 주소
    private int complaintCount;   // 유저 신고 누적수
    private int followingCount;   // 유저 팔로잉 수
    private int followerCount;    // 유저 팔로워 수
}