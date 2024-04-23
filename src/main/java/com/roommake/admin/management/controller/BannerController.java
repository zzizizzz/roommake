package com.roommake.admin.management.controller;

import com.roommake.admin.management.dto.BannerForm;
import com.roommake.admin.management.service.BannerService;
import com.roommake.admin.management.vo.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/management/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/create")
    @ResponseBody
    public String createBanner(BannerForm bannerForm) {
        bannerService.createBanner(bannerForm);

        return "redirect:/admin/management/banner";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public Banner detail(@PathVariable("id") int id) {

        return bannerService.getBannerById(id);
    }
}
