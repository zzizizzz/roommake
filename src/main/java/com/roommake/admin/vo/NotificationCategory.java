package com.roommake.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationCategory {

    private int id;      // 알림 카테고리 번호
    private String name; // 알림 카테고리 이름
}
