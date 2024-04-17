package com.roommake.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqCategory {

    private int id;         // 질문카테고리번호
    private String name;    // 질문카테고리이름
}
