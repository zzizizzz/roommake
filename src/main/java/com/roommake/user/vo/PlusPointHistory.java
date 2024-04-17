package com.roommake.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PlusPointHistory {

    private int id;                // 적립 포인트 번호
    private int amount;            // 적립 포인트 양
    private Date createDate;       // 포인트 적립 날짜
    private Date expireDate;       // 포인트 소멸예정일
    private User userId;           // 유저 번호
    private int balance;           // 가용 포인트
    private PointType pointTypeId; // 포인트 유형 번호
}
