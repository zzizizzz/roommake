package com.roommake.admin.management.mapper;

import com.roommake.admin.management.vo.Banner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerMapper {
    void createBanner(Banner banner);

    Banner getBannerById(int id);

    List<Banner> getAllBanners();
}
