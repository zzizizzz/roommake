package com.roommake.admin.management.service;

import com.roommake.admin.management.dto.NoticeForm;
import com.roommake.admin.management.mapper.NoticeMapper;
import com.roommake.admin.management.vo.Notice;
import com.roommake.dto.Criteria;
import com.roommake.dto.ListDto;
import com.roommake.dto.Pagination;
import com.roommake.user.mapper.UserMapper;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;
    private final UserMapper userMapper;

    public void createNotice(NoticeForm form, String email) {

        User user = userMapper.getUserByEmail(email);

        Notice notice = new Notice();
        notice.setTitle(form.getTitle());
        notice.setContent(form.getContent());
        notice.setPriority(form.getPriority());
        notice.setCreateByUser(user);
        notice.setUpdateByUser(user);

        noticeMapper.createNotice(notice);
    }

    public Notice modifyNotice(int id, NoticeForm form, String email) {

        Notice notice = noticeMapper.getNoticeById(id);
        User user = userMapper.getUserByEmail(email);

        notice.setTitle(form.getTitle());
        notice.setContent(form.getContent());
        notice.setPriority(form.getPriority());
        notice.setUpdateByUser(user);
        notice.setUpdateDate(new Date());
        notice.setDeleteYn("N");
        noticeMapper.modifyNotice(notice);

        return notice;
    }

    public void deleteNotice(int id) {
        Notice notice = noticeMapper.getNoticeById(id);
        notice.setDeleteYn("Y");
        notice.setDeleteDate(new Date());
        noticeMapper.modifyNotice(notice);
    }

    public ListDto<Notice> getNotices(Criteria criteria) {

        int totalRows = noticeMapper.getTotalRows(criteria);

        Pagination pagination = new Pagination(criteria.getPage(), totalRows, criteria.getRows());

        criteria.setBegin(pagination.getBegin());
        criteria.setEnd(pagination.getEnd());

        List<Notice> noticeList = noticeMapper.getNotices(criteria);

        ListDto<Notice> dto = new ListDto<Notice>(noticeList, pagination);
        return dto;
    }

    public Notice getNoticeById(int id) {
        return noticeMapper.getNoticeById(id);
    }
}
