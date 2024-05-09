package com.roommake.admin.order.controller;

import com.roommake.admin.order.dto.AdminExchangeDto;
import com.roommake.admin.order.dto.ItemCancelDto;
import com.roommake.admin.order.dto.ItemReturnDto;
import com.roommake.admin.order.dto.OrderHistoryResponseDto;
import com.roommake.admin.order.service.AdminOrderService;
import com.roommake.admin.refund.AdminRefundDto;
import com.roommake.order.vo.Order;
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
        List<AdminExchangeDto> exchanges = adminOrderService.getAllExchanges();
        model.addAttribute("exchanges", exchanges);
        return "admin/order/exchange";
    }

    //교환 상세페이지
    @GetMapping("/exchange-detail/{id}")
    public String exchangedetail(Model model, @PathVariable Long id) {
        AdminExchangeDto exchangeById = adminOrderService.getExchangeById(id);
        model.addAttribute("exchange", exchangeById);
        return "admin/order/exchange-detail";
    }

    // 교환 업데이트
    @GetMapping("/exchange/{id}")
    public String updateExchangeApprovalYn(@PathVariable int id) {
        adminOrderService.updateExchangeApprovalYn(id);
        return "redirect:/admin/order/exchange";
    }

    // 환불리스트
    @GetMapping("/refund")
    public String refund(Model model) {
        List<AdminRefundDto> refund = adminOrderService.getAllRefund();
        model.addAttribute("refunds", refund);
        return "admin/order/refund";
    }

    //반품페이지
    @GetMapping("/item-return")
    public String returnpage(Model model) {
        List<ItemReturnDto> itemReturnDtoList = adminOrderService.getAllItemReturn();
        model.addAttribute("itemReturnList", itemReturnDtoList);
        return "admin/order/item-return";
    }

    // 반품 업데이트
    @PostMapping("/updateReturn")
    @ResponseBody
    public int updateReturn(@RequestBody List<ItemReturnDto> ItemReturnDtoList) {
        String itemReturnStatus = "반품완료";
        String itemReturnYn = "Y";

        List<Integer> itemReturnId = ItemReturnDtoList.stream()
                .map(ItemReturnDto::getItemReturnId)
                .toList();

        int updateReturnYnResult = adminOrderService.updateReturnYn(itemReturnStatus, itemReturnYn, itemReturnId);
        return updateReturnYnResult;
    }

    @GetMapping("/orderCancel")
    public String orderCannel(Model model) {
        List<ItemCancelDto> orderCancel = adminOrderService.getAllorderCancel();
        model.addAttribute("orderCancels", orderCancel);
        return "admin/order/orderCancel";
    }

    @PostMapping("/item")
    public void orderItem(@RequestBody Order order) {
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
