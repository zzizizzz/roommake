package com.roommake.cs.controller;

import com.roommake.admin.management.service.NoticeService;
import com.roommake.admin.management.vo.Notice;
import com.roommake.dto.Criteria;
import com.roommake.dto.ListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/cs")
@Controller
@RequiredArgsConstructor
public class CsController {

    private final NoticeService noticeService;

    Criteria criteria = new Criteria();

    @GetMapping("/notice/list")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
                       @RequestParam(name = "sort", required = false, defaultValue = "date") String sort,
                       @RequestParam(name = "opt", required = false) String opt,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       Model model) {

        criteria.setPage(page);
        criteria.setRows(rows);
        criteria.setSort(sort);

        if (StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
            criteria.setOpt(opt);
            criteria.setKeyword(keyword);
        }

        ListDto<Notice> dto = noticeService.getNotices(criteria);
        model.addAttribute("noticeList", dto.getItems());
        model.addAttribute("paging", dto.getPaging());
        model.addAttribute("criteria", criteria);

        return "cs/notice/list";
    }

    @GetMapping("/notice/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "cs/notice/detail";
    }
}