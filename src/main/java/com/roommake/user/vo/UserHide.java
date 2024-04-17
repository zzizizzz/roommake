package com.roommake.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserHide {

    private int id;          // 사용자 숨김 번호
    private Date createDate; // 사용자 숨김 날짜
    private User userId;     // 숨긴 유저 번호
    private User hideUserId; // 숨겨진 유저 번호
}
