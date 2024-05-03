package com.roommake.admin.order.controller;

import com.roommake.admin.order.dto.OrderHistoryResponseDto;
import com.roommake.admin.order.service.AdminOrderService;
import com.roommake.admin.refund.AdminRefundDto;
import com.roommake.order.vo.Exchange;
import com.roommake.order.vo.Order;
import com.roommake.order.vo.OrderCancel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {
    private final AdminOrderService adminOrderService;

    //교환페이지
    @GetMapping("/exchange")
    public String exchange(Model model) {
        List<Exchange> exchanges = adminOrderService.getAllExchanges();
        model.addAttribute("exchanges", exchanges);
        return "admin/order/exchange";
    }

    //교환 상세페이지
    @GetMapping("/exchange-detail")
    public String exchangedetail() {
        return "admin/order/exchange-detail";
    }

    @PostMapping("/exchange")
    public String updateExchange(Model model) {
        int id = (int) model.getAttribute("id");
        adminOrderService.updateExchange(id);
        return "redirect:admin/order/exchange";
    }

    // 환불리스트
    @GetMapping("/refund")
    public String refund(Model model) {
        List<AdminRefundDto> refund = adminOrderService.getAllRefund();
        model.addAttribute("refunds", refund);
        return "admin/order/refund";
    }

    //반품페이지
    @GetMapping("/return")
    public String returnpage(Model model) {

        return "admin/order/return";
    }

    // 취소 페이지
    @GetMapping("/orderCancel")
    public String channel(Model model) {
        List<OrderCancel> OrderCancels = adminOrderService.getAllorderCancel();
        model.addAttribute("OrderCancel", OrderCancels);
        return "admin/order/orderCancel";
    }

    @PostMapping("/item")
    @ResponseBody
    public void orderItem(@RequestBody Order order) {
        // 송장번호 랜덤번호
        // Random random = new Random();
        // String postNum = (random.nextInt(900) + 100) + "-" + (random.nextInt(900) + 100);
        // System.out.println(postNum);

        adminOrderService.updateOrderStatus(order);
    }

    //주문내역 리스트
    @GetMapping("/item")
    public String order(Model model) {
        List<OrderHistoryResponseDto> orders = adminOrderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/order/item";
    }
}
