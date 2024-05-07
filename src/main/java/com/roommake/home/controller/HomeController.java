package com.roommake.home.controller;

import com.roommake.admin.management.vo.Banner;
import com.roommake.dto.Criteria;
import com.roommake.event.service.EventService;
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
    private final EventService eventService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = homeService.getNewProducts();
        Criteria criteria = new Criteria();
        criteria.setFilt("active");
        List<Banner> banners = eventService.getBanners(criteria);
        model.addAttribute("products", products);
        model.addAttribute("banners", banners);
        return "home";
    }
}
