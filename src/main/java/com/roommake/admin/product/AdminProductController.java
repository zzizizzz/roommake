package com.roommake.admin.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/product")
public class AdminProductController {
    @GetMapping("/insert")
    public String insert() {
        return "admin/product/insert";
    }

    //상품수정폼
    @GetMapping("/modify")
    public String modify() {
        return "admin/product/modify";
    }

    //상품 상세정보
    @GetMapping("/detail")
    public String detail() {
        return "admin/product/detail";
    }
}