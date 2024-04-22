package com.roommake.admin.management.service;

import com.roommake.admin.management.dto.BannerForm;
import com.roommake.admin.management.mapper.BannerMapper;
import com.roommake.admin.management.vo.Banner;
import com.roommake.user.vo.User;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerMapper bannerMapper;

    @Value("${event.upload.save.directory}")
    private String directory;

    public void createBanner(BannerForm bannerForm) {
        String imageName = FileUtils.upload(bannerForm.getImageFile(), directory);
        User user = new User();
        user.setId(1);
        Banner banner = Banner.builder()
                .user(user)
                .description(bannerForm.getDescription())
                .startDate(bannerForm.getStartDate())
                .endDate(bannerForm.getEndDate())
                .imageName(imageName)
                .url(bannerForm.getUrl())
                .build();
        bannerMapper.createBanner(banner);
    }

    public Banner getBannerById(int id) {
        return bannerMapper.getBannerById(id);
    }

    public List<Banner> getAllBanners() {
        return bannerMapper.getAllBanners();
    }
}
