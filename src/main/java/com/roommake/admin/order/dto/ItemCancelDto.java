package com.roommake.admin.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ItemCancelDto {
    private int cancelId;
    private int Id;
    private Date createDate;
    private Date updtaeDate;
    private int ReasonId;
    private String ReasonName;
}
