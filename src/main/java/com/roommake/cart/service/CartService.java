package com.roommake.cart.service;

import com.roommake.cart.mapper.CartMapper;
import com.roommake.product.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    public List<Product> getNewProducts() {
        return cartMapper.getNewProducts();
    }
}
