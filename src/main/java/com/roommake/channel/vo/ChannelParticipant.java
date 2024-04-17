package com.roommake.channel.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelParticipant {

    private Channel channelId;  // 채널번호
    private User userId;        // 유저번호
}
