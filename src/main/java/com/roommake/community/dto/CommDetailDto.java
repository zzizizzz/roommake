package com.roommake.community.dto;

import com.roommake.community.vo.Community;
import com.roommake.community.vo.ComplaintCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommDetailDto {
    List<ComplaintCategory> complaintCategories;
    Community community;
    private boolean isLike;
    private boolean isScrap;
}
