package com.roommake.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Visit {

    private Date date;  // 방문자
    private int count;  // 누적방문수
    private String ip;  // 방문자 ip

}
