package com.roommake.admin.management.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Notice {

    private int id;              // 공지사항 번호
    private String title;        // 공지사항 제목
    private String content;      // 공지사항 내용
    private Date createDate;     // 공지 작성일
    private Date updateDate;     // 공지 수정일
    private String deleteYn;     // 공지 삭제여부
    private int priority;        // 공지사항 우선순위
    private User createByUserId; // 공지사항 작성자
    private User updateByUserId; // 공지사항 수정자
}
