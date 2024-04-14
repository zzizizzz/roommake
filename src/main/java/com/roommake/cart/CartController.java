package com.roommake.cart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String cart() {
        return "cart/cart";
    }

    @PostMapping("/order/form")
    public String orderform() {
        return "order/form";
    }
}
