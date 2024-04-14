package com.roommake.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order/claim")
public class ClaimController {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/cancel-form")
    public String cancel() {
        return "order/claim/cancel-form";
    }

    @GetMapping("/pay/cancel")
    public String cancelCompleted(String tid, Model model) {
        log.info("결제 고유번호: " + tid);

        // 카카오 취소 요청하기
        CancelResponse cancelResponse = kakaoPayService.payCancel(tid);

        return "redirect:/order/claim/cancel-completed";
    }

    // 취소완료 UI 테스트용, 추후 삭제 예정 (카카오페이 취소 거치지 않고 바로 진입)
    @GetMapping("/cancel-completed")
    public String cancelCompleted() {
        return "order/claim/cancel-completed";
    }

    @GetMapping("/cancel-detail")
    public String cancelDetail() {
        return "order/claim/cancel-detail";
    }

    @GetMapping("/return-exchange-form")
    public String returnExchangeForm() {
        return "order/claim/return-exchange-form";
    }

    @GetMapping("/return-completed")
    public String returnCompleted() {
        return "order/claim/return-completed";
    }
    @GetMapping("/return-detail")
    public String returnDetail() {
        return "order/claim/return-detail";
    }
    @GetMapping("/exchange-completed")
    public String exchangeCompleted() {
        return "order/claim/exchange-completed";
    }
    @GetMapping("/exchange-detail")
    public String exchangeDetail() {
        return "order/claim/exchange-detail";
    }
}
