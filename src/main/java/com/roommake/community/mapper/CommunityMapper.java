package com.roommake.community.mapper;

import com.roommake.community.dto.CommCriteria;
import com.roommake.community.dto.MyPageCommunity;
import com.roommake.community.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {

    int getCommTotalRowByCommId(CommCriteria criteria);

    List<Community> getAllCommunitiesByCatId(CommCriteria criteria);

    List<CommunityCategory> getAllCommCategories();

    void createCommunity(Community community);

    Community getCommunityByCommId(int commId);

    List<ComplaintCategory> getComplaintCategories();

    CommunityLike getCommLikeUser(CommunityLike commLikeUser);

    CommunityScrap getCommScrapUser(CommunityScrap commScrapUser);

    void modifyCommunity(Community community);

    void addCommunityLike(CommunityLike commLikeUser);

    void deleteCommunityLike(CommunityLike commLikeUser);

    void createCommunityComplaint(CommunityComplaint communityComplaint);

    // 사용자 ID로 게시글 정보 조회
    List<MyPageCommunity> getCommunitiesByUserId(int userId);

    // 사용자 ID로 사용자가 작성한 총 게시물 수 조회
    int countCommunitiesByUserId(int userId);

    // 사용자 ID로 사용자가 작성한 게시글의 총 댓글 수 조회
    int countRepliesByUserId(int userId);
}
