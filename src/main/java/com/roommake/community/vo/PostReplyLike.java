package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostReplyLike {

    private User userId;        // 유저번호
    private PostReply replyId;  // 채널글 댓글번호

}
