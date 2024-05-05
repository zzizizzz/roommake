package com.roommake.admin.refund;

import java.util.Date;

public record AdminRefundDto(
        int id, Date createDate, Date updateDate, String status,
        int amount, String userNickname
) {

}
