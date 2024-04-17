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
    private String status;                      // 교환처리상태
    private OrderItem orderItemId;              // 주문상세번호
    private String approvalYn;                  // 교환승인여부
    private ExchangeReason ReasonId;            // 교환사유번호
    private Date createDate;                    // 교환생성일
    private Date updateDate;                    // 교환수정일
}
