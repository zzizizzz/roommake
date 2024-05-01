package com.roommake.order.mapper;

import com.roommake.order.dto.OrderCancelDto;
import com.roommake.order.dto.OrderItemDto;
import com.roommake.order.dto.ReturnExchangeCreateForm;
import com.roommake.order.dto.ReturnExchangeDto;
import com.roommake.order.vo.*;
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

    List<ReturnExchangeReason> getAllReturnExchangeReasons();

    void createItemReturn(ReturnExchangeCreateForm form);

    ReturnExchangeDto getItemReturnByOrderItemId(int id);

    Delivery getReturnCollectionDeliveryByOrderItemId(int id);

    ReturnExchangeReason getReturnReasonByReturnId(int id);
}
