package com.roommake.product.controller;

import com.roommake.product.service.ProductService;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품홈으로 이동하는 메소드
    @GetMapping("/home")
    public String store() {
        return "store/home";
    }

    // 상품디테일로 이동하는 메소드
    @GetMapping("/detail")
    public String detail() {
        return "store/product-detail";
    }

    /**
     * 상품리스트를 불러오는 메소드
     *
     * @param model
     * @return 전테 상품리스트
     */
    @GetMapping("/category")
    public String list(Model model) {
        List<ProductTag> prodTags = productService.getProductTagAll();
        model.addAttribute("prodTags", prodTags);

        List<Product> product = productService.getProductAll();
        model.addAttribute("product", product);

        return "store/category-list";
    }

    // 스크랩 popup으로 이동하는 메소드
    @GetMapping("/popup")
    public String popup() {
        return "layout/scrap-popup";
    }
}
