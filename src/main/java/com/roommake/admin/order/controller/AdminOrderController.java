package com.roommake.admin.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/order")
public class AdminOrderController {
    //교환페이지
    @GetMapping("/exchange")
    public String exchange() {
        return "admin/order/exchange";
    }

    // 환불리스트
    @GetMapping("/refund")
    public String refund() {
        return "admin/order/refund";
    }

    //교환 상세페이지
    @GetMapping("/exchange-detail")
    public String exchangedetail() {
        return "admin/order/exchange-detail";
    }

    //반품페이지
    @GetMapping("/return")
    public String returnpage() {
        return "admin/order/return";
    }
}
