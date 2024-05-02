package com.roommake.admin.Dashboard.service;

import com.roommake.admin.Dashboard.dto.DashboardDto;
import com.roommake.admin.Dashboard.mapper.DashboardMapper;
import com.roommake.admin.management.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final QnaService qnaService;
    private final DashboardMapper dashboardMapper;

    public DashboardDto getAdminHomeDto() {

        LocalDate today = LocalDate.now();
        String todayStr = today.toString();
        LocalDate yesterDay = today.minusDays(1);
        String yesterDayStr = yesterDay.toString();

        DashboardDto dto = new DashboardDto();

        dto.setSalesDataList(dashboardMapper.getSalesData(yesterDayStr, 7));

        dto.setNoAnswerQnas(qnaService.getNoAnswerQnas());

        dto.setNewUserCnt(dashboardMapper.getNewUserCnt(yesterDayStr));
        dto.setTotalUserCnt(dashboardMapper.getUserCntByDate(todayStr));

        return dto;
    }
}
