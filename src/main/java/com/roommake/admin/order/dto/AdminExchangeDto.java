package com.roommake.admin.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminExchangeDto {
    private int exchangeId;
    private String status;
    private String approvalYn;
    private String reasonName;
    private String OrderItemId;
    private Date createDate;
    private int productId;
    private String productName;
    private String userNickname;
    private String deliveryAddress;
    private int afterId;
    private int beforeId;
}
