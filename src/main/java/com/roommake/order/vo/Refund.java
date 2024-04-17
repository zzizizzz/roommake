package com.roommake.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Refund {

    private int id;            // 환불번호
    private Date refundDate;   // 환불접수일자
    private String status;     // 환불처리상태
    private int amount;        // 환불금액
    private ItemReturn returnId;   // 반품번호
    private Payment paymentId; // 결제번호
}
