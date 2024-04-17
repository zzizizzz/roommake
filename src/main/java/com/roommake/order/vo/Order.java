package com.roommake.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Order {

    private int id;               // 주문 번호
    private int UserId;           // 주문자
    private Date createDate;      // 주문 일자
    private Date updateDate;      // 주문 변경 일자
    private int totalPrice;       // 총 주문금액
    private Delivery deliveryId;  // 배송지 번호
    private int usePoint;         // 사용 포인트
    private int paymentPrice;     // 결제금액
    private OrderStatus statusId; // 주문상태 번호
}
