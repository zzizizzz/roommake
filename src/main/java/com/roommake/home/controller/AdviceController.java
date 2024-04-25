package com.roommake.home.controller;

import com.roommake.product.service.ProductService;
import com.roommake.product.vo.ProductCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final ProductService productService;

    @ModelAttribute("categories")
    public List<ProductCategory> category() {
        return productService.getProductMainCategories();
    }
}
