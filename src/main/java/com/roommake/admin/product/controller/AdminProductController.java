package com.roommake.admin.product.controller;

import com.roommake.admin.product.form.ProductCreateForm;
import com.roommake.admin.product.service.AdminProductService;
import com.roommake.product.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/product")
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductService adminproductService;

    // 상품등록폼
    @GetMapping("/create")
    public String productform() {
        return "admin/product/form";
    }

    // 상품등록
    @PostMapping("/create")
    public String createproduct(ProductCreateForm productCreateForm) {
        adminproductService.insertProduct(productCreateForm);
        return "redirect:/admin/product/list";
    }

    //이미지등록
    public String create(ProductCreateForm form) {
        System.out.println(form);
        return "redirect:/product/list";
    }

    //상품수정폼
    @GetMapping("/modify")
    public String modify() {
        return "admin/product/modify";
    }

    //상품 상세정보
    @GetMapping(path = "/detail")
    public String detail(int no, Model model) {
        Product product = adminproductService.getProductDetail(no);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }
}