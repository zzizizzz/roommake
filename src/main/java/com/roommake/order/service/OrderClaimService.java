package com.roommake.order.service;

import com.roommake.order.dto.OrderDto;
import com.roommake.order.dto.OrderItemDto;
import com.roommake.order.mapper.OrderClaimMapper;
import com.roommake.order.mapper.OrderMapper;
import com.roommake.order.vo.Delivery;
import com.roommake.order.vo.OrderCancelReason;
import com.roommake.order.vo.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderClaimService {

    private final OrderClaimMapper orderClaimMapper;
    private final OrderMapper orderMapper;

    /**
     * 모든 주문취소사유를 반환한다.
     *
     * @return 모든 주문취소사유
     */
    public List<OrderCancelReason> getAllCancelReasons() {

        return orderClaimMapper.getAllCancelReasons();
    }

    public OrderDto getOrderClaimByOrderId(int orderId, int orderItemId) {

        Payment payment = orderMapper.getPaymentByOrderId(orderId);
        Delivery delivery = orderMapper.getDeliveryByOrderId(orderId);
        OrderItemDto item = orderClaimMapper.getItemByOrderItemId(orderItemId);

        OrderDto orderDto = orderMapper.getOrderById(orderId);
        orderDto.setPayment(payment);
        orderDto.setDelivery(delivery);
        orderDto.setItem(item);

        return orderDto;
    }
}
