package com.roommake.order.mapper;

import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.dto.CartItemDto;
import com.roommake.order.vo.Delivery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    CartItemDto getProductsByDetailId(CartCreateForm cartCreateForm);

    Delivery getDefaultDeliveryByUserId(int id);
}
