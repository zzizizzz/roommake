package com.roommake.order.service;

import com.roommake.order.dto.OrderListDto;
import com.roommake.order.mapper.MyOrderMapper;
import com.roommake.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyOrderService {

    private final MyOrderMapper myOrderMapper;
    private final OrderMapper orderMapper;

    /**
     * 로그인한 유저의 모든 주문내역을 반환한다.
     *
     * @param userId 유저번호
     * @return 로그인한 유저의 모든 주문내역
     */
    public List<OrderListDto> getAllOrdersByUserId(int userId, String beginDate, String endDate) {

        return myOrderMapper.getAllOrdersByUserId(userId, beginDate, endDate);
    }
}
