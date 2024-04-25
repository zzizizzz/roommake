package com.roommake.channel.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelPostLike {

    private int postId;    // 채널글번호
    private int userId;    // 유저번호
}
