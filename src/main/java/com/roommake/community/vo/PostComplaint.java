package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PostComplaint {

    private int id;                         // 신고번호
    private Post postId;                    // 채널글번호
    private User userId;                    // 신고한 유저번호
    private ComplaintCategory categoryId;   // 신고 카테고리번호
    private Date createDate;                // 신고생성일
    private Date updateDate;                // 신고수정일
    private String complaintYn;             // 신고 승인여부

}
