package com.roommake.admin.order.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderHistoryResponseDto {
    private int id;
    private int productId;
    private int detailId;
    private int price;
    private int qty;
    private int status;
    private String productName;
    private String userNickname;
    private String invoiceNumber;
}


