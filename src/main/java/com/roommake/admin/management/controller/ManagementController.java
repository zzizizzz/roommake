package com.roommake.admin.management.controller;

import com.roommake.admin.management.service.NoticeService;
import com.roommake.admin.management.vo.Notice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin/management")
@Controller
@RequiredArgsConstructor
@Tag(name = "관리 API", description = "관리자 페이지 중 사이트 관리 관련 CRUD API를 제공한다.")
public class ManagementController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    @Operation(summary = "전체 공지사항 조회", description = "전체 공지사항을 조회한다.")
    public String notice(Model model) {
        List<Notice> noticeList = noticeService.getNotices();
        model.addAttribute("noticeList", noticeList);
        return "admin/management/notice";
    }

    @GetMapping("/faq")
    @Operation(summary = "전체 자주묻는질문 조회", description = "전체 자주묻는질문 조회한다.")
    public String faq() {
        return "admin/management/faq";
    }

    @GetMapping("/qna")
    @Operation(summary = "전체 문의사항 조회", description = "전체 문의사항 내역을 조회한다.")
    public String qna() {
        return "admin/management/qna";
    }

    @GetMapping("/banner")
    public String banner() {
        return "admin/management/banner";
    }

    @GetMapping("/complaint")
    public String complaint() {
        return "admin/management/complaint";
    }
}
