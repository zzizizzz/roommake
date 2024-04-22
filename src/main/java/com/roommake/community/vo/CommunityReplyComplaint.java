package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CommunityReplyComplaint {

    private int id;                         // 신고번호
    private User userId;                    // 유저번호
    private CommunityReply commReplyId;     // 커뮤니티 댓글번호
    private ComplaintCategory categoryId;   // 신고 카테고리번호
    private Date createDate;                // 신고일
    private String complaintYn;             // 신고승인여부
    private String complaintDeleteYn;             // 신고삭제여부
}
