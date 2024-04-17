package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CommunityComplaint {

    private int id;                         // 신고번호
    private User userId;                    // 유저번호
    private Community commId;               // 커뮤니티번호
    private ComplaintCategory categoryId;   // 신고 카테고리번호
    private Date createDate;                // 신고생성일
    private Date updateDate;                // 신고수정일
    private String complaintYn;             // 신고 승인여부

}
