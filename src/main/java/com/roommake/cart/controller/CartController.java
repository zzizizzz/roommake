package com.roommake.cart.controller;

import com.roommake.cart.dto.CartItemDto;
import com.roommake.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "장바구니 API", description = "장바구니에 대한 추가, 변경, 삭제, 조회 API를 제공한다.")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니", description = "장바구니를 조회한다.")
    @GetMapping("/cart")
    public String cart(Model model) {
        List<CartItemDto> dto = cartService.getCartsByUserId(2);
        model.addAttribute("items", dto);
        return "cart/cart";
    }

    @Operation(summary = "주문/결제 폼", description = "장바구니 선택 품목에 대한 주문/결제 폼을 조회한다.")
    @PostMapping("/order/form")
    public String orderform(Model model) {
        return "order/form";
    }
}
