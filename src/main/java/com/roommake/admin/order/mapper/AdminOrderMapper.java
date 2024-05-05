package com.roommake.admin.order.mapper;

import com.roommake.admin.order.dto.AdminExchangeDto;
import com.roommake.admin.order.dto.ItemReturnDto;
import com.roommake.admin.order.dto.OrderHistoryResponseDto;
import com.roommake.admin.refund.AdminRefundDto;
import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.dto.CartItemDto;
import com.roommake.order.dto.OrderDto;
import com.roommake.order.dto.OrderItemDto;
import com.roommake.order.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminOrderMapper {
    CartItemDto getProductsByDetailId(CartCreateForm cartCreateForm);

    Delivery getDefaultDeliveryByUserId(int id);

    CartItemDto getProductDetailByDetailId(int id);

    int createOrder(Order order);

    void createOrderItem(OrderItem orderItem);

    void createPayment(Payment payment);

    OrderDto getOrderById(int orderId);

    Payment getPaymentByOrderId(int orderId);

    Delivery getDeliveryByOrderId(int orderId);

    List<OrderItemDto> getItemsByOrderId(int orderId);

    void updateExchangeApprovalYn(int id);

    List<OrderHistoryResponseDto> getAllOrders();

    int updateOrderStatus(Order order);

    List<AdminRefundDto> getRefund();

    List<AdminExchangeDto> getAllExchanges();

    List<OrderStatus> getAllOrderStatus();

    List<OrderCancel> getAllorderCancels();

    List<ItemReturnDto> getAllItemReturn();

    int updateReturnYn(String itemReturnStatus, String itemReturnYn, List<Integer> itemReturnId);
}
