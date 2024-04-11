package com.roommake.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class ProductController {


    @GetMapping("/home")
    public String store() {
        return "store/home";
    }

}
