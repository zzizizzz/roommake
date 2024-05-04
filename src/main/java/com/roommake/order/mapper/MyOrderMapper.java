package com.roommake.order.mapper;

import com.roommake.order.dto.OrderListDto;
import com.roommake.order.vo.OrderItem;
import com.roommake.order.vo.OrderStatus;
import com.roommake.order.vo.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyOrderMapper {

    List<OrderListDto> getAllOrdersByUserId(@Param("userId") int userId, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    List<OrderItem> getOrderItemsByOrderId(int id);

    Payment getPaymentByOrderId(int id);

    OrderStatus getOrderStatusByOrderId(int id);
}
