package com.roommake.admin.management.mapper;

import com.roommake.admin.management.vo.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    void createNotice(Notice notice);

    List<Notice> getAllNotices();

    Notice getNoticeById(int id);

    void modifyNotice(Notice notice);
}
