package com.roommake.community.vo;

import com.roommake.user.vo.ScrapFolder;
import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CommunityScrap {

    private Community commId;           // 커뮤니티번호
    private User userId;                // 유저번호
    private ScrapFolder scrapFolderId;  // 스크랩폴더번호
    private Date createDate;            // 스크랩 최초생성일
    private Date updateDate;            // 스크랩 수정일
    private Date deleteDate;            // 스크랩 삭제일
    private String deleteYn;            // 스크랩 삭제여부

}
