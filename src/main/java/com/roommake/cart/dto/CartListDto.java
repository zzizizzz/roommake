package com.roommake.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartListDto {
    private List<CartItemDto> items;

    public int getTotalPrice() {
        int total = 0;
        for (CartItemDto item : items) {
            int itemPrice = item.getPrice() * item.getAmount();
            total += itemPrice;
        }
        return total; // 총 상품금액
    }

    public int getTotalDiscountPrice() {
        int total = 0;
        for (CartItemDto item : items) {
            total += (int) (item.getPrice() * item.getDiscount() / 100.0);
        }
        return total; // 총 할인금액
    }

    public int getDeliveryPay() {
        return 0; // 배송비
    }

    public int getTotalPayPrice() {
        return getTotalPrice() - getTotalDiscountPrice(); // 총 결제금액
    }
}
