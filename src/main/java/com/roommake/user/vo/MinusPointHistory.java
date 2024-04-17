package com.roommake.user.vo;

import com.roommake.order.vo.Payment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class MinusPointHistory {

    private int id;                // 차감 포인트 번호
    private int amount;            // 차감 포인트 양
    private Date date;             // 포인트 차감 날짜
    private User userId;           // 유저번호
    private Payment paymentId;     // 결제번호
    private PointType pointTypeId; // 포인트 유형 번호
}
