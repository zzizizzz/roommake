package com.roommake.home.controller;

import com.roommake.admin.management.service.BannerService;
import com.roommake.admin.management.vo.Banner;
import com.roommake.home.service.HomeService;
import com.roommake.product.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final BannerService bannerService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = homeService.getNewProducts();
        List<Banner> banners = bannerService.getAllBanners();
        model.addAttribute("products", products);
        model.addAttribute("banners", banners);
        return "home";
    }
}
