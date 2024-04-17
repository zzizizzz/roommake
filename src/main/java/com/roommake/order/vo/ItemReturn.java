package com.roommake.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ItemReturn {

    private int id;                // 반품번호
    private Date createDate;       // 반품신청일
    private Date updateDate;       // 반품수정일
    private String status;         // 반품처리상태
    private OrderItem orderItemId; // 주문상세번호
    private String returnYn;       // 반품승인여부
    private ItemReturnReason reasonId; // 반품사유 번호
}
