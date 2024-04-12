package com.roommake.order;

import com.roommake.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/pay/ready")
    public @ResponseBody ReadyResponse payReady(int quantity, int totalPrice, Model model) {
        log.info("주문 수량: " + quantity);
        log.info("주문 금액: " + totalPrice);

        // 카카오 결제 준비하기
        ReadyResponse readyResponse = kakaoPayService.payReady(quantity, totalPrice);
        // 세션에 결제 고유번호(tid) 저장
        SessionUtils.addAttribute("tid", readyResponse.getTid());
        log.info("결제 고유번호: " + readyResponse.getTid());

        return readyResponse;
    }

    @GetMapping("/pay/completed")
    public String payCompleted(@RequestParam("pg_token") String pgToken) {
        String tid = SessionUtils.getStringAttributeValue("tid");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("결제 고유번호: " + tid);

        // 카카오 결제 요청하기
        ApproveResponse approveResponse = kakaoPayService.payApprove(tid, pgToken);

        return "redirect:/order/pay/completed" + tid;
    }

    @GetMapping("/completed")
    public String completed() {
        return "order/completed";
    }

    @GetMapping("/detail")
    public String detail() {
        return "order/detail";
    }
}
