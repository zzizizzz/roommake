package com.roommake.admin.management.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Banner {

    private int id;             // 배너번호
    private User userId;        // 등록자
    private Date createDate;    // 배너 등록일
    private Date deleteDate;    // 배너 삭제일
    private Date startDate;     // 배너 게시시작일
    private Date endDate;       // 배너 게시종료일
    private String status;      // 배너 상태
    private String imageName;   // 배너 이미지명
    private String deleteYn;    // 배너 삭제여부
    private String reason;      // 배너 설명
}