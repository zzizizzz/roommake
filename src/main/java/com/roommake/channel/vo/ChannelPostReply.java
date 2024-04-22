package com.roommake.channel.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ChannelPostReply {

    private int id;             // 댓글번호 
    private ChannelPost1 postId; // 채널글번호
    private User userId;        // 유저번호
    private String content;     // 댓글 내용
    private Date createDate;    // 댓글 등록일
    private Date updateDate;    // 댓글 수정일
    private Date deleteDate;    // 댓글 삭제일
    private String status;      // 댓글 상태
    private String deleteUYn;   // 댓글 삭제여부
    private int likeCount;      // 댓글 좋아요수
    private int complaintCount; // 댓글 신고수
    private int parentsId;      // 부모 댓글번호
}
