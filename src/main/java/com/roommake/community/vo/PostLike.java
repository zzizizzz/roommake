package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostLike {

    private Post postId;    // 채널글번호
    private User userId;    // 유저번호

}
