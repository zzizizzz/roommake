package com.roommake.admin.management.controller;

import com.roommake.admin.management.dto.BannerForm;
import com.roommake.admin.management.service.BannerService;
import com.roommake.admin.management.vo.Banner;
import com.roommake.resolver.Login;
import com.roommake.user.security.LoginUser;
import com.roommake.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/management/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;
    private final S3Uploader s3Uploader;

    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String create(@Login LoginUser loginUser, BannerForm bannerForm) {
        String imageName = "default.jpg";
        if (bannerForm.getImageFile() != null) {
            imageName = s3Uploader.saveFile(bannerForm.getImageFile());
        }
        bannerService.createBanner(bannerForm, imageName, loginUser.getId());

        return "redirect:/admin/management/banner";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public Banner detail(@PathVariable("id") int id) {

        return bannerService.getBannerById(id);
    }

    @PostMapping("/modify/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Banner modify(@PathVariable("id") int bannerId, @Login LoginUser loginUser, BannerForm bannerForm) {

        String imageName = "";
        if (bannerForm.getImageFile() != null) {
            imageName = s3Uploader.saveFile(bannerForm.getImageFile());
        }
        return bannerService.modifyBanner(bannerId, bannerForm, imageName, loginUser.getId());
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody List<Integer> bannerIds) {

        for (Integer bannerId : bannerIds) {
            Banner banner = bannerService.getBannerById(bannerId);
            bannerService.deleteBanner(banner.getId());
        }
        return "redirect:/admin/management/notice";
    }
}
