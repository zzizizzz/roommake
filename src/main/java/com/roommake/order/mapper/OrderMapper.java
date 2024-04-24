package com.roommake.order.mapper;

import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.dto.CartItemDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    CartItemDto getProductsByDetailId(CartCreateForm cartCreateForm);
}
