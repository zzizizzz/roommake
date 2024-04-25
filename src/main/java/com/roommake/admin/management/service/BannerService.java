package com.roommake.admin.management.service;

import com.roommake.admin.management.dto.BannerForm;
import com.roommake.admin.management.mapper.BannerMapper;
import com.roommake.admin.management.vo.Banner;
import com.roommake.dto.Criteria;
import com.roommake.dto.ListDto;
import com.roommake.dto.Pagination;
import com.roommake.user.vo.User;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
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
                //.imageName(getImageName(bannerForm))
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

    public Banner modifyBanner(int id, BannerForm bannerForm) {

        Banner banner = bannerMapper.getBannerById(id);
        User user = new User();
        user.setId(1);
        banner.setStartDate(bannerForm.getStartDate());
        banner.setEndDate(bannerForm.getEndDate());
        if (bannerForm.getImageFile() != null) {
            String imageName = getImageName(bannerForm);
            banner.setImageOriginName((imageName));
        }
        banner.setDescription(bannerForm.getDescription());
        banner.setUrl(bannerForm.getUrl());
        banner.setDeleteYn("N");
        bannerMapper.modifyBanner(banner);

        return banner;
    }

    private String getImageName(BannerForm bannerForm) {

        return FileUtils.upload(bannerForm.getImageFile(), directory);
    }

    public void deleteBanner(int id) {

        Banner banner = bannerMapper.getBannerById(id);
        banner.setDeleteYn("Y");
        banner.setDeleteDate(new Date());
        bannerMapper.modifyBanner(banner);
    }

    public ListDto<Banner> getBanners(Criteria criteria) {

        int totalRows = bannerMapper.getTotalRows(criteria);
        Pagination pagination = new Pagination(criteria.getPage(), totalRows, criteria.getRows());

        criteria.setBegin(pagination.getBegin());
        criteria.setEnd(pagination.getEnd());

        List<Banner> bannerList = bannerMapper.getBanners(criteria);
        ListDto<Banner> dto = new ListDto<>(bannerList, pagination);
        return dto;
    }
}
