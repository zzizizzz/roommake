package com.roommake.order.mapper;

import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.dto.CartItemDto;
import com.roommake.order.dto.OrderCreateForm;
import com.roommake.order.vo.Delivery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    CartItemDto getProductsByDetailId(CartCreateForm cartCreateForm);

    Delivery getDefaultDeliveryByUserId(int id);

    CartItemDto getProductDetailByDetailId(int id);

    void createOrder(@Param("form") OrderCreateForm orderCreateForm, @Param("userId") int userId);
}
