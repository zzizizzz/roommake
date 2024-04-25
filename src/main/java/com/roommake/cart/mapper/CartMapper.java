package com.roommake.cart.mapper;

import com.roommake.cart.dto.CartItemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<CartItemDto> getCartsByUserId(int userId);

    List<CartItemDto> getItemOptionsByProductId(int productId);
}
