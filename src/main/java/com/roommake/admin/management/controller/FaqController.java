package com.roommake.admin.management.controller;

import com.roommake.admin.management.dto.FaqForm;
import com.roommake.admin.management.service.FaqService;
import com.roommake.admin.management.vo.Faq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin/management/faq")
@RequiredArgsConstructor
@Tag(name = "관리 API", description = "자주묻는질문 추가, 변경, 삭제, 조회 API를 제공한다.")
public class FaqController {

    private final FaqService faqService;

    @Operation(summary = "자주묻는질문 추가", description = "자주묻는질문을 추가한다.")
    @PostMapping("/create")
    @ResponseBody
    public String create(FaqForm form, Principal principal) {

        faqService.createFaq(form, principal.getName());

        return "redirect:/admin/management/faq";
    }

    @PostMapping("/modify/{id}")
    @ResponseBody
    public Faq modify(@PathVariable("id") int id, FaqForm form, Principal principal) {

        return faqService.modifyFaq(id, form, principal.getName());
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody List<Integer> faqIds) {
        for (Integer faqId : faqIds) {
            Faq faq = faqService.getFaqById(faqId);
            faqService.deleteFaq(faq.getId());
        }
        return "redirect:/admin/management/faq";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public Faq detail(@PathVariable("id") int id) {
        return faqService.getFaqById(id);
    }
}
