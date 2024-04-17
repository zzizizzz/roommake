package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommunityReplyLike {

    private CommunityReply commReplyId;     // 커뮤니티 댓글번호
    private User userId;                    // 유저번호

}
