package com.roommake.cs.controller;

import com.roommake.admin.management.service.NoticeService;
import com.roommake.admin.management.vo.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/cs")
@Controller
@RequiredArgsConstructor
public class CsController {

    private final NoticeService noticeService;

    @GetMapping("/notice/list")
    public String list(Model model) {

        List<Notice> noticeList = noticeService.getNotices();
        model.addAttribute("noticeList", noticeList);

        return "cs/notice/list";
    }

    @GetMapping("/notice/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "cs/notice/detail";
    }
}
