package com.roommake.order.controller;

import com.roommake.order.dto.CancelResponse;
import com.roommake.order.dto.OrderDto;
import com.roommake.order.service.KakaoPayService;
import com.roommake.order.service.OrderClaimService;
import com.roommake.order.service.OrderService;
import com.roommake.order.vo.OrderCancelReason;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order/claim")
@Tag(name = "주문취소/반품/환불 API", description = "주문취소/반품/환불에 대한 추가, 변경, 삭제, 조회 API를 제공한다.")
public class OrderClaimController {

    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;
    private final OrderClaimService orderClaimService;

    @Operation(summary = "주문전체취소 신청 폼", description = "주문전체취소 신청 폼을 조회한다.")
    @GetMapping("/cancel-form/{orderId}")
    public String allCancel(@PathVariable("orderId") int orderId, Model model) {

        OrderDto dto = orderService.getOrderById(orderId);
        List<OrderCancelReason> reasons = orderClaimService.getAllCancelReasons();

        model.addAttribute("dto", dto);
        model.addAttribute("reasons", reasons);

        return "order/claim/cancel-form";
    }

    @Operation(summary = "개별 주문취소 신청 폼", description = "개별 주문취소 신청 폼을 조회한다.")
    @GetMapping("/cancel-form/{orderId}/{orderItemId}")
    public String cancel(@PathVariable("orderId") int orderId, @PathVariable("orderItemId") int orderItemId, Model model) {

        OrderDto dto = orderClaimService.getOrderClaimByOrderId(orderId, orderItemId);
        List<OrderCancelReason> reasons = orderClaimService.getAllCancelReasons();

        model.addAttribute("dto", dto);
        model.addAttribute("reasons", reasons);

        return "order/claim/cancel-form";
    }

    @Operation(summary = "카카오페이 결제취소", description = "카카오페이 결제취소 요청 및 취소승인 후 주문취소완료 페이지로 이동한다.")
    @GetMapping("/pay/cancel")
    @ResponseBody
    public ResponseEntity<CancelResponse> cancelCompleted(String tid, int totalPrice, Model model) {
        log.info("결제 고유번호: " + tid);
        log.info("전체취소 금액: " + totalPrice);

        // 카카오페이 취소 요청하기
        CancelResponse cancelResponse = kakaoPayService.payCancel(tid, totalPrice);
        return ResponseEntity.ok(cancelResponse);
    }

    // 취소완료 UI 테스트용, 추후 삭제 예정 (카카오페이 취소 거치지 않고 바로 진입)
    @GetMapping("/cancel-completed")
    public String cancelCompleted() {
        return "order/claim/cancel-completed";
    }

    @Operation(summary = "주문취소 상세", description = "주문취소 상세내역을 조회한다.")
    @GetMapping("/cancel-detail")
    public String cancelDetail() {
        return "order/claim/cancel-detail";
    }

    @Operation(summary = "반품/교환 신청 폼", description = "반품/교환 신청 폼을 조회한다.")
    @GetMapping("/return-exchange-form/{orderId}/{orderItemId}")
    public String returnExchangeForm(@PathVariable("orderId") int orderId, @PathVariable("orderItemId") int orderItemId, Model model) {

        OrderDto dto = orderClaimService.getOrderClaimByOrderId(orderId, orderItemId);

        model.addAttribute("dto", dto);

        return "order/claim/return-exchange-form";
    }

    @Operation(summary = "반품신청 완료", description = "반품신청 완료내역을 조회한다.")
    @GetMapping("/return-completed")
    public String returnCompleted() {
        return "order/claim/return-completed";
    }

    @Operation(summary = "반품 상세", description = "반품 상세내역을 조회한다.")
    @GetMapping("/return-detail")
    public String returnDetail() {
        return "order/claim/return-detail";
    }

    @Operation(summary = "교환신청 완료", description = "교환신청 완료내역을 조회한다.")
    @GetMapping("/exchange-completed")
    public String exchangeCompleted() {
        return "order/claim/exchange-completed";
    }

    @Operation(summary = "교환 상세", description = "교환 상세내역을 조회한다.")
    @GetMapping("/exchange-detail")
    public String exchangeDetail() {
        return "order/claim/exchange-detail";
    }
}
