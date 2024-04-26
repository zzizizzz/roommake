package com.roommake.admin.management.service;

import com.roommake.admin.management.dto.BannerForm;
import com.roommake.admin.management.mapper.BannerMapper;
import com.roommake.admin.management.vo.Banner;
import com.roommake.dto.Criteria;
import com.roommake.dto.ListDto;
import com.roommake.dto.Pagination;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerMapper bannerMapper;

    public void createBanner(BannerForm bannerForm, String imageName, int userId) {
        User user = User.builder().id(userId).build();
        String originName = "";
        if (!bannerForm.getImageFile().isEmpty()) {
            originName = bannerForm.getImageFile().getOriginalFilename();
        }
        Banner banner = Banner.builder()
                .user(user)
                .description(bannerForm.getDescription())
                .startDate(bannerForm.getStartDate())
                .endDate(bannerForm.getEndDate())
                .imageOriginName(originName)
                .imageUploadName(imageName)
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

    public Banner modifyBanner(int id, BannerForm bannerForm, String imageName, int userId) {

        Banner banner = bannerMapper.getBannerById(id);
        User user = User.builder().id(userId).build();

        banner.setStartDate(bannerForm.getStartDate());
        banner.setEndDate(bannerForm.getEndDate());
        if (bannerForm.getImageFile() != null) {
            banner.setImageOriginName(bannerForm.getImageFile().getOriginalFilename());
            banner.setImageUploadName(imageName);
        }
        banner.setDescription(bannerForm.getDescription());
        banner.setUrl(bannerForm.getUrl());
        banner.setDeleteYn("N");
        bannerMapper.modifyBanner(banner);

        return banner;
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
