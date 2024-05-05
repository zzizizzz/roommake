package com.roommake.admin.Dashboard.service;

import com.roommake.admin.Dashboard.dto.DashboardDto;
import com.roommake.admin.Dashboard.dto.OrderStatusData;
import com.roommake.admin.Dashboard.mapper.DashboardMapper;
import com.roommake.admin.Dashboard.vo.SalesData;
import com.roommake.admin.management.service.ComplaintService;
import com.roommake.admin.management.service.QnaService;
import com.roommake.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final QnaService qnaService;
    private final DashboardMapper dashboardMapper;
    private final OrderMapper orderMapper;
    private final ComplaintService complaintService;

    LocalDate today = LocalDate.now();
    String todayStr = today.toString();
    LocalDate yesterDay = today.minusDays(1);
    String yesterDayStr = yesterDay.toString();

    public DashboardDto getAdminHomeDto() {
        DashboardDto dto = new DashboardDto();

        dto.setOrderStatusDataList(getOrderStatusData(todayStr));   // 주문상태별 건수 데이터

        dto.setSalesDataList(getSalesData(yesterDayStr, 6));        // 어제부터 일주일치 매출 데이터

        dto.setNoConfirmComplaints(complaintService.getBoardComplaints("N"));   // 미처리 게시글 신고
        dto.setNoConfirmComplaints(complaintService.getReplyComplaints("N"));   // 미처리 댓글 신고

        dto.setNoAnswerQnas(qnaService.getNoAnswerQnas());              // 미응답 문의사항

        dto.setNewUserCnt(dashboardMapper.getNewUserCnt(yesterDayStr));    // 신규가입자
        dto.setTotalUserCnt(dashboardMapper.getUserCntByDate(todayStr));    // 누적 가입자

        return dto;
    }

    public List<SalesData> getSalesData(String date, int days) {

        return dashboardMapper.getSalesData(date, days);
    }

    public List<OrderStatusData> getOrderStatusData(String date) {

        return dashboardMapper.getOrderStatusData(date);
    }
}
