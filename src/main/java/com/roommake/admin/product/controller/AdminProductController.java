package com.roommake.admin.product.controller;

import com.roommake.admin.product.form.ProductCreateForm;
import com.roommake.admin.product.form.ProductDetailForm;
import com.roommake.admin.product.service.AdminProductService;
import com.roommake.product.service.ProductService;
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
    private final ProductService productService;

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
        return "redirect:/product/list";
    }

    //상품수정폼
    @GetMapping("/modify")
    public String modify(int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "/admin/product/modify";
    }

    // 상품 수정폼
    @PostMapping("/modify")
    public String modifyPost(Product product) {
        adminproductService.modifyProduct(product);
        return "redirect:/admin/product/list";
    }

    //상품 상세정보
    @GetMapping("/detail")
    public String detail(int id, Model model) {
        adminproductService.detailSearch(id, model);
        return "admin/product/detail";
    }

    // 상품 상세 정보입력
    @PostMapping("/detail")
    public String detailproduct(ProductDetailForm productDetailForm, Model model) {
        adminproductService.insertProductDetailAndSearch(productDetailForm, model);
        return "admin/product/detail";
    }
}