package com.roommake.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Exchange {

    private int id;                      // 교환번호
    private String status;               // 교환처리상태
    private OrderItem orderItem;         // 주문상세
    private String approvalYn;           // 교환승인여부
    private ReturnExchangeReason reason; // 교환사유
    private Date createDate;             // 교환생성일
    private Date updateDate;             // 교환수정일
    private Delivery collectionDelivery; // 상품 회수지
    private Delivery reDelivery;         // 상품 재배송지
}
