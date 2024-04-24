package com.roommake.product.controller;

import com.roommake.cart.dto.CartCreateForm;
import com.roommake.product.service.ProductService;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductCategory;
import com.roommake.product.vo.ProductDetail;
import com.roommake.product.vo.ProductTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "상품들의 조회 API를 제공한다.")
public class ProductController {

    private final ProductService productService;

    // 상품홈으로 이동하는 메소드
    @GetMapping("/home")
    public String store(Model model) {

        List<ProductCategory> productCategory = productService.getAllProductCategories();
        model.addAttribute("productCategory", productCategory);

        return "store/home";
    }

    // 상품디테일로 이동하는 메소드
    @Operation(summary = "해당상품 상세 정보조회", description = "해당 상품의 정보를 조회한다")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        List<ProductDetail> productDetail = productService.getProductDetailById(id);
        model.addAttribute("productDetail", productDetail);

        return "store/product-detail";
    }

    /**
     * 상품리스트를 불러오는 메소드
     *
     * @param model
     * @return 전체 상품리스트
     */
    @GetMapping("/category/{id}")
    public String list(@PathVariable int id, Model model) {
        List<ProductTag> prodTags = productService.getAllProductTags();
        model.addAttribute("prodTags", prodTags);

        List<Product> product = productService.getProductsById(id);
        model.addAttribute("product", product);

        List<ProductCategory> productCategory = productService.getAllProductCategories();
        model.addAttribute("productCategory", productCategory);

        return "store/category-list";
    }

    @PostMapping("/addCart")
    public String addCart(@RequestParam("id") int id, @RequestParam("productDetailId") List<Integer> details, @RequestParam("amount") List<Integer> amounts) {

        List<CartCreateForm> cartFormList = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            CartCreateForm form = new CartCreateForm();
            form.setProductId(id);
            form.setProductDetailId(details.get(i));
            form.setAmount(amounts.get(i));

            cartFormList.add(form);
        }

        productService.createCart(cartFormList);

        return String.format("redirect:detail/%d", id);
    }

    // 스크랩 popup으로 이동하는 메소드
    @GetMapping("/popup")
    public String popup() {
        return "layout/scrap-popup";
    }
}
