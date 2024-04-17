package com.roommake.admin.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Faq {

    private int id;                 // 질문번호
    private String title;           // 질문제목
    private String content;         // 질문내용
    private Date createDate;        // 질문등록일
    private Date updateDate;        // 질문수정일
    private String deleteYn;        // 질문삭제여부
    private FaqCategory categoryId; // 질문카테고리번호
    private User createByUserId;    // 질문작성자
    private User updateByUserId;    // 질문수정자

}
