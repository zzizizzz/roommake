package com.roommake.cart.controller;

import com.roommake.cart.dto.CartItemDto;
import com.roommake.cart.dto.CartListDto;
import com.roommake.cart.service.CartService;
import com.roommake.product.service.ProductService;
import com.roommake.resolver.Login;
import com.roommake.user.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(name = "장바구니 API", description = "장바구니에 대한 추가, 변경, 삭제, 조회 API를 제공한다.")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "장바구니", description = "장바구니를 조회한다.")
    @GetMapping()
    public String cart(@Login LoginUser loginUser, Model model) {

        List<CartItemDto> items = cartService.getCartsByUserId(loginUser.getId());
        CartListDto dto = new CartListDto(items);

        model.addAttribute("dto", dto);
        return "cart/cart";
    }

    @GetMapping("/option/{productId}")
    @ResponseBody
    public List<CartItemDto> getOptions(@PathVariable("productId") int productId, Model model) {

        List<CartItemDto> productDetails = cartService.getItemOptionsByProductId(productId);
        model.addAttribute("details", productDetails);

        return productDetails;
    }
}
