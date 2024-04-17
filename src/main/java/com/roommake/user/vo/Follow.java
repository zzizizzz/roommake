package com.roommake.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Follow {

    private User followerUserId;    // 팔로우하는유저
    private User followeeUserId;    // 팔로우받는유저

}
