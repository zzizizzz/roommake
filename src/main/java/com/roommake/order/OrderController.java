package com.roommake.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @GetMapping("/orderform")
    public String orderform() {
        return "order/orderform";
    }
}
