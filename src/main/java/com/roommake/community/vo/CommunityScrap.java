package com.roommake.community.vo;

import com.roommake.user.vo.ScrapFolder;
import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommunityScrap {

    private Community commId;           // 커뮤니티번호
    private User userId;                // 유저번호
    private ScrapFolder scrapFolderId;  // 스크랩폴더번호

}
