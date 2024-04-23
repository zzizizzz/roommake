package com.roommake.cart.service;

import com.roommake.cart.dto.CartItemDto;
import com.roommake.cart.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    public List<CartItemDto> getCartsByUserId(int userId) {
        return cartMapper.getCartsByUserId(userId);
    }
}
