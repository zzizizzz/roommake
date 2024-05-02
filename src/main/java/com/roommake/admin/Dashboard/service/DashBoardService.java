package com.roommake.admin.Dashboard.service;

import com.roommake.admin.Dashboard.dto.DashboardDto;
import com.roommake.admin.Dashboard.dto.OrderStatusData;
import com.roommake.admin.Dashboard.mapper.DashboardMapper;
import com.roommake.admin.Dashboard.vo.SalesData;
import com.roommake.admin.management.service.QnaService;
import com.roommake.order.mapper.OrderMapper;
import com.roommake.order.vo.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final QnaService qnaService;
    private final DashboardMapper dashboardMapper;
    private final OrderMapper orderMapper;

    LocalDate today = LocalDate.now();
    String todayStr = today.toString();
    LocalDate yesterDay = today.minusDays(1);
    String yesterDayStr = yesterDay.toString();

    public DashboardDto getAdminHomeDto() {

        DashboardDto dto = new DashboardDto();

        dto.setOrderStatusDataList(getOrderStatusData(yesterDayStr));

        dto.setSalesDataList(getSalesData(yesterDayStr, 7));    // 전일부터 일주일치 데이터 조회

        dto.setNoAnswerQnas(qnaService.getNoAnswerQnas());

        dto.setNewUserCnt(dashboardMapper.getNewUserCnt(yesterDayStr));
        dto.setTotalUserCnt(dashboardMapper.getUserCntByDate(todayStr));

        return dto;
    }

    public List<SalesData> getSalesData(String date, int days) {
        return dashboardMapper.getSalesData(date, days);
    }

    public List<OrderStatusData> getOrderStatusData(String date) {
        List<OrderStatus> statusList = orderMapper.getAllOrderStatus();

        List<OrderStatusData> orderStatusDataList = new ArrayList<>();

        for (OrderStatus status : statusList) {
            OrderStatusData data = dashboardMapper.getOrderStatusData(date, status.getId());

            if (data != null) {
                orderStatusDataList.add(data);
            }
        }
        return orderStatusDataList;
    }
}
