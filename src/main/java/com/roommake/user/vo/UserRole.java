package com.roommake.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRole {

    private int id;      // 유저권한 번호
    private User userId; // 유저 번호
    private String name; // 권한 이름
}
