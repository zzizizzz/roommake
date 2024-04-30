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
                               @RequestParam("qnaId") int id,
                               Principal principal) {
        qnaService.updateAnswer(id, answer, principal.getName());

        return "redirect:/admin/management/qna";
    }

// 답변 등록 다 되면 할 예정! 무시해주세요
//    public Qna writeAnswer(@RequestParam("answer") String answer,
//                           Principal principal,
//                           Model model) {
//        qnaService.updateAnswer();
//    }

//    @RequestMapping("/mailSend")
//    public void mailSend(@RequestBody QnaMailDto mailDto, QnaAnswerForm form) {
//        mailDto.setSubject(form.getTitle());
//        mailDto.setTo("realgyj@gmail.com");
//        mailDto.setTemplate("admin/management/answer-email");
//
//        Map mailMap = new HashMap();
//        mailMap.put("qna", "어떤 제목");
//        mailDto.setContext(mailMap);
//        try {
//            qnaService.sendMail(mailDto);
//        } catch (Exception ex) {
//            throw new RuntimeException("이메일발송실패");
//        }
//    }
}
