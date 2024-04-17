package com.roommake.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaCategory {

    private int id;      // 문의사항 카테고리 번호
    private String name; // 문의사항 카테고리 이름
}
