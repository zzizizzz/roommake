package com.roommake.admin.Dashboard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesData {

    private int id;
    private Date date;
    private int salesCnt;
    private long salesAmount;
}
