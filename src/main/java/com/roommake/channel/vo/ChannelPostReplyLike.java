package com.roommake.channel.vo;

import com.roommake.channel.vo.ChannelPostReply;
import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChannelPostReplyLike {

    private User userId;               // 유저번호
    private ChannelPostReply replyId;  // 채널글 댓글번호
}
