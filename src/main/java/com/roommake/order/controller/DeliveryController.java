package com.roommake.order.controller;

import com.roommake.order.dto.DeliveryForm;
import com.roommake.order.service.DeliveryService;
import com.roommake.order.vo.Delivery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order/delivery")
@Tag(name = "배송지 API", description = "배송지에 대한 추가, 변경, 삭제, 조회 API를 제공한다.")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "배송지 리스트 팝업", description = "배송지 리스트를 조회한다.")
    @GetMapping("/list")
    public String deliveryList(Model model) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByUserId(4);
        model.addAttribute("deliveries", deliveries);
        return "order/deliverylist";
    }

    @Operation(summary = "배송지 추가 폼 팝업", description = "배송지 추가 폼을 조회한다.")
    @GetMapping("/form")
    public String deliveryForm(Model model) {
        model.addAttribute("deliveryForm", new DeliveryForm());

        return "order/deliveryform";
    }

    @Operation(summary = "배송지 추가", description = "배송지 추가 후 배송지 리스트 페이지로 이동한다.")
    @PostMapping("/form")
    public String createDelivery(@Valid DeliveryForm deliveryForm, BindingResult errors) {
        if (errors.hasErrors()) {
            return "order/deliveryform";
        }

        deliveryService.createDelivery(deliveryForm);
        return "redirect:/order/delivery/list";
    }
}
