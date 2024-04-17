package com.roommake.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Exchange {

    private int id;                             // 교환번호
    private Date date;                          // 교환일
    private String status;                      // 교환처리상태
    private OrderItem orderItemId;              // 주문상세번호
    private String exchangeYn;                  // 교환승인여부
    private ExchangeReason exchangeReasonId;    // 교환사유번호

}
