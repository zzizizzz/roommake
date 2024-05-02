package com.roommake.admin.Dashboard.mapper;

import com.roommake.admin.Dashboard.vo.SalesData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardMapper {

    List<SalesData> getSalesData(@Param("date") String date,
                                 @Param("days") int days);

    // 신규가입자수
    int getNewUserCnt(String date);

    // 누적 가입자수
    int getUserCntByDate(String date);
}
