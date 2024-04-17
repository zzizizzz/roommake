package com.roommake.channel.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelPostLike {

    private Channelpost postId;    // 채널글번호
    private User userId;    // 유저번호
}
