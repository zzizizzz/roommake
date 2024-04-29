package com.roommake.order.service;

import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.dto.CartItemDto;
import com.roommake.order.mapper.OrderMapper;
import com.roommake.order.vo.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    /**
     * 장바구니에 담긴 상품의 정보를 반환한다.
     *
     * @param forms 장바구니에 담긴 상품의 정보가 포함된 CartCreateForm 객체 리스트
     * @return 장바구니에 담긴 상품의 정보가 포함된 CartItemDto 객체 리스트
     */
    public List<CartItemDto> getProductsByDetailId(List<CartCreateForm> forms) {

        List<CartItemDto> list = new ArrayList<>();
        for (CartCreateForm form : forms) {
            CartItemDto dto = orderMapper.getProductsByDetailId(form);
            // form에서 받아온 상품수량을 dto에 저장
            dto.setAmount(form.getAmount());
            list.add(dto);
        }

        return list;
    }

    /**
     * 로그인한 유저의 기본 배송지를 반환한다.
     *
     * @param userId 유저 번호
     * @return 로그인한 유저의 기본 배송지
     */
    public Delivery getDefaultDeliveryByUserId(int userId) {
        return orderMapper.getDefaultDeliveryByUserId(userId);
    }
}
