package com.roommake.admin.management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeForm {

    private String title;
    private String content;
    private int priority;
}
