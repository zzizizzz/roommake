package com.roommake.admin.management.vo;

import com.roommake.community.vo.ComplaintCategory;
import com.roommake.user.vo.User;
import lombok.Data;

import java.util.Date;

@Data
public class Complaint {
    private int id;
    private User user;
    private int contentId;
    private ComplaintCategory category;
    private Date createDate;
    private Date updateDate;
    private String complaintYn;
    private String deleteYn;
}
