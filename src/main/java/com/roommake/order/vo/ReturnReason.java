package com.roommake.order.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReturnReason {

    private int id;      // 반품사유 번호
    private String name; // 반품사유 이름
}
