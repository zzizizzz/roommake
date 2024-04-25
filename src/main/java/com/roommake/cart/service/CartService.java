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

    /**
     * 로그인한 유저의 장바구니 상품 정보를 반환한다.
     *
     * @param userId 유저번호
     * @return 로그인한 유저의 장바구니 상품 목록
     */
    public List<CartItemDto> getCartsByUserId(int userId) {
        return cartMapper.getCartsByUserId(userId);
    }

    /**
     * 장바구니 상품의 상품 상세정보를 반환한다.
     *
     * @param productId 상품번호
     * @return 상품번호에 해당하는 옵션 목록 및 상품 상세정보
     */
    public List<CartItemDto> getItemOptionsByProductId(int productId) {
        return cartMapper.getItemOptionsByProductId(productId);
    }
}
