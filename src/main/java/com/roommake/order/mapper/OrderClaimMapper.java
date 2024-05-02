package com.roommake.order.mapper;

import com.roommake.order.dto.OrderCancelDto;
import com.roommake.order.dto.OrderItemDto;
import com.roommake.order.dto.ReturnExchangeCreateForm;
import com.roommake.order.dto.ReturnExchangeDto;
import com.roommake.order.vo.*;
import com.roommake.product.vo.ProductDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderClaimMapper {

    List<OrderCancelReason> getAllCancelReasons();

    OrderItemDto getOrderItemDtoByOrderItemId(int orderItemId);

    void createOrderCancel(Order order);

    void createCancelRefund(Refund refund);

    void updateCancelOrderStatus(int orderId);

    void updateCancelOrderItemStatus(int orderId);

    OrderCancelDto getOrderCancelByOrderId(int orderId);

    Refund getRefundByPaymentId(int paymentId);

    OrderCancelReason getCancelReasonByCancelId(int orderCancelId);

    List<ReturnExchangeReason> getAllReturnExchangeReasons();

    void createItemReturn(ReturnExchangeCreateForm form);

    ReturnExchangeDto getItemReturnByOrderItemId(int id);

    Delivery getReturnCollectionDeliveryByReturnId(int id);

    ReturnExchangeReason getReturnReasonByReturnId(int id);

    void updateReturnOrderItemStatus(int id);

    OrderItem getOrderItemByOrderItemId(int id);

    ReturnExchangeReason getReturnExchangeReasonById(int id);

    ProductDetail getProductDetailById(int id);

    void createExchange(Exchange exchange);

    void createExchangeDetail(ExchangeDetail exchangeDetail);

    void updateExchangeOrderItemStatus(int id);

    ReturnExchangeReason getExchangeReasonByExchangeId(int id);

    ExchangeDetail getExchangeDetailByExchangeId(int id);

    ReturnExchangeDto getExchangeByOrderItemId(int id);
}
