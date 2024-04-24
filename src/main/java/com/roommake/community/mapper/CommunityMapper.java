package com.roommake.community.mapper;

import com.roommake.community.vo.Community;
import com.roommake.community.vo.CommunityCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {
    List<CommunityCategory> getAllCommCategories();

    void createCommunity(Community community);

    List<Community> getAllCommunitiesByCatId(int commCatId);

    Community getCommunityByCommId(int commId);
}
