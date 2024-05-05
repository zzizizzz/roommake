package com.roommake.admin.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ItemReturnDto {

    private int itemReturnId;

    private Date itemReturnCreateDate;

    private Date itemReturnUpdateDate;

    private String itemReturnStatus;

    private int orderItemId;

    private String itemReturnYn;

    private int returnExchangeReasonId;

    private int collectionDeliveryId;

    private String returnExchangeReasonName;

    private String returnExchangeDetailReason;

    private int orderId;

    private int orderItemPrice;

    private String userNickname;

    private String productName;
}
