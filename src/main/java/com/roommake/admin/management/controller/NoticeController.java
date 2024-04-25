package com.roommake.admin.management.controller;

import com.roommake.admin.management.dto.NoticeForm;
import com.roommake.admin.management.service.NoticeService;
import com.roommake.admin.management.vo.Notice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/management/notice")
@RequiredArgsConstructor
@Tag(name = "관리 API", description = "공지사항 추가, 변경, 삭제, 조회 API를 제공한다.")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 추가", description = "공지사항을 추가한다.")
    @PostMapping("/create")
    @ResponseBody
    public String create(@Valid NoticeForm form, Principal principal) {

        noticeService.createNotice(form, principal.getName());

        return "redirect:/admin/management/notice";
    }

    @PostMapping("/modify/{id}")
    @ResponseBody
    public Notice modify(@PathVariable("id") int id, NoticeForm form, Principal principal) {

        return noticeService.modifyNotice(id, form, principal.getName());
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody List<Integer> noticeIds) {
        for (Integer noticeId : noticeIds) {
            Notice notice = noticeService.getNoticeById(noticeId);
            noticeService.deleteNotice(notice.getId());
        }
        return "redirect:/admin/management/notice";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public Notice detail(@PathVariable("id") int id) {
        return noticeService.getNoticeById(id);
    }
}
