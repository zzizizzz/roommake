package com.roommake.order.mapper;

import com.roommake.order.dto.OrderCancelDto;
import com.roommake.order.dto.OrderItemDto;
import com.roommake.order.vo.Order;
import com.roommake.order.vo.OrderCancelReason;
import com.roommake.order.vo.Refund;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderClaimMapper {

    List<OrderCancelReason> getAllCancelReasons();

    OrderItemDto getItemByOrderItemId(int orderItemId);

    void createOrderCancel(Order order);

    void createCancelRefund(Refund refund);

    void updateOrderStatus(int orderId);

    void updateOrderItemStatus(int orderId);

    OrderCancelDto getOrderCancelByOrderId(int orderId);

    Refund getRefundByPaymentId(int paymentId);

    OrderCancelReason getCancelReasonByCancelId(int orderCancelId);
}
