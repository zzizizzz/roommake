package com.roommake.admin.management.service;

import com.roommake.admin.management.dto.NoticeForm;
import com.roommake.admin.management.mapper.NoticeMapper;
import com.roommake.admin.management.vo.Notice;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public void createNotice(NoticeForm form) {

        User user = new User();
        //user.setEmail(username); User 객체 생성 메소드 구현 후 수정 예정
        user.setId(1);

        Notice notice = new Notice();
        notice.setTitle(form.getTitle());
        notice.setContent(form.getContent());
        notice.setPriority(form.getPriority());
        notice.setCreateByUser(user);
        notice.setUpdateByUser(user);

        noticeMapper.createNotice(notice);
    }

    public Notice modifyNotice(int id, NoticeForm form) {

        Notice notice = noticeMapper.getNoticeById(id);
        User user = new User();
        //user.setEmail(username); User 객체 생성 메소드 구현 후 수정 예정
        user.setId(1);

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

    public List<Notice> getNotices() {
        return noticeMapper.getAllNotices();
    }

    public Notice getNoticeById(int id) {
        return noticeMapper.getNoticeById(id);
    }
}
