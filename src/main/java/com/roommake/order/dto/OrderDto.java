package com.roommake.order.dto;

import com.roommake.order.vo.Delivery;
import com.roommake.order.vo.Payment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderDto {

    private int orderId;       // 주문 번호
    private Date createDate;   // 주문 생성일자
    private Date updateDate;   // 주문 수정일자
    private int totalPrice;    // 총 주문금액
    private int paymentPrice;  // 결제금액
    private Payment payment;   // 결제
    private Delivery delivery; // 배송지
    List<OrderItemDto> items;  // 상품정보, 상품상세정보가 담긴 객체 배열
}
