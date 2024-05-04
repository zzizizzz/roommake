package com.roommake.cs.controller;

import com.roommake.admin.management.service.FaqService;
import com.roommake.admin.management.service.NoticeService;
import com.roommake.admin.management.service.QnaService;
import com.roommake.admin.management.vo.Faq;
import com.roommake.admin.management.vo.FaqCategory;
import com.roommake.admin.management.vo.Notice;
import com.roommake.admin.management.vo.QnaCategory;
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

import java.util.List;

@RequestMapping("/cs")
@Controller
@RequiredArgsConstructor
public class CsController {

    private final NoticeService noticeService;
    private final FaqService faqService;
    private final QnaService qnaService;

    @GetMapping("/notice/list")
    public String noticeList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
                             @RequestParam(name = "sort", required = false, defaultValue = "date") String sort,
                             @RequestParam(name = "opt", required = false) String opt,
                             @RequestParam(name = "keyword", required = false) String keyword,
                             Model model) {

        List<FaqCategory> faqCategories = faqService.getFaqCategories();
        model.addAttribute("faqCategories", faqCategories);

        Criteria criteria = new Criteria();

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
        List<FaqCategory> faqCategories = faqService.getFaqCategories();
        model.addAttribute("faqCategories", faqCategories);

        Notice notice = noticeService.getNoticeById(id);
        model.addAttribute("notice", notice);
        return "cs/notice/detail";
    }

    @GetMapping("/faq/list")
    public String fnqList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                          @RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
                          @RequestParam(name = "sort", required = false, defaultValue = "date") String sort,
                          @RequestParam(name = "filt", required = false, defaultValue = "total") String filt,
                          @RequestParam(name = "opt", required = false) String opt,
                          @RequestParam(name = "keyword", required = false) String keyword,
                          Model model) {

        List<FaqCategory> faqCategories = faqService.getFaqCategories();
        model.addAttribute("faqCategories", faqCategories);

        Criteria criteria = new Criteria();

        criteria.setPage(page);
        criteria.setRows(rows);
        criteria.setSort(sort);
        criteria.setFilt(filt);

        if (StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
            criteria.setOpt(opt);
            criteria.setKeyword(keyword);
        }

        ListDto<Faq> dto = faqService.getFaqs(criteria);
        model.addAttribute("faqs", dto.getItems());
        model.addAttribute("paging", dto.getPaging());
        model.addAttribute("criteria", criteria);

        return "cs/faq/list";
    }

    @GetMapping("/qna/form")
    public String qnaForm(Model model) {
        List<FaqCategory> faqCategories = faqService.getFaqCategories();
        model.addAttribute("faqCategories", faqCategories);

        List<QnaCategory> qnaCategories = qnaService.getQnaCategories();
        model.addAttribute("qnaCategories", qnaCategories);
        
        return "cs/qna/form";
    }
}
