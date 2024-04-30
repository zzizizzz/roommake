package com.roommake.order.service;

import com.roommake.order.mapper.OrderClaimMapper;
import com.roommake.order.vo.OrderCancelReason;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderClaimService {

    private final OrderClaimMapper orderClaimMapper;

    /**
     * 모든 주문취소사유를 반환한다.
     *
     * @return 모든 주문취소사유
     */
    public List<OrderCancelReason> getAllCancelReasons() {

        return orderClaimMapper.getAllCancelReasons();
    }
}
