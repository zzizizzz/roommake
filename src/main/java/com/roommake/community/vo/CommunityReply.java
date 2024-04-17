package com.roommake.community.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CommunityReply {

    private int id;                 // 댓글번호
    private Community commId;       // 커뮤니티번호
    private User userId;            // 유저번호
    private String content;         // 댓글내용
    private Date createDate;        // 댓글등록일
    private Date updateDate;        // 댓글수정일
    private String status;          // 댓글상태
    private String deleteYn;        // 댓글삭제여부
    private int likeCount;          // 댓글좋아요수
    private int complaintCount;     // 댓글신고수
    private int parentsId;          // 부모댓글번호

}
