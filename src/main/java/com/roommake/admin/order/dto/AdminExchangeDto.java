package com.roommake.admin.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminExchangeDto {
    private int Id;
    private int exchangeId;
    private String status;
    private String exchangeStatus;
    private int OrderItemId;
    private int returnExchangeReasonId;
    private Date createDate;
    private int returnChangeReasonId;
    private String ReasonName;
    private String DetailReason;
}
