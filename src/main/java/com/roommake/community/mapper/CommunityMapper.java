package com.roommake.community.mapper;

import com.roommake.community.dto.CommCriteria;
import com.roommake.community.dto.MyPageCommunity;
import com.roommake.community.vo.Community;
import com.roommake.community.vo.CommunityCategory;
import com.roommake.community.vo.ComplaintCategory;
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

    // 사용자 ID로 게시글 정보 조회
    List<MyPageCommunity> getCommunitiesByUserId(int userId);

    // 사용자 ID로 사용자가 작성한 총 게시물 수 조회
    int countCommunitiesByUserId(int userId);

    // 사용자 ID로 사용자가 작성한 게시글의 총 댓글 수 조회
    int countRepliesByUserId(int userId);
}
