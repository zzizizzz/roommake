package com.roommake.admin.management.controller;

import com.roommake.admin.management.service.QnaService;
import com.roommake.admin.management.vo.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/management/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/detail/{id}")
    @ResponseBody
    public Qna detail(@PathVariable("id") int id) {
        Qna qna = qnaService.getQnaById(id);
        return qna;
    }

    @PostMapping("/updateAnswer")
    public String updateAnswer(@RequestParam("answer") String answer,
                               @RequestParam("noAnswerQna") int id,
                               Principal principal) throws Exception {
        qnaService.updateAnswer(id, answer, principal.getName());
        return "redirect:/admin/management/qna";
    }
}
