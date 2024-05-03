package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommunityLike {

    private Community commId;   // 커뮤니티번호
    private User userId;        // 유저번호

}
