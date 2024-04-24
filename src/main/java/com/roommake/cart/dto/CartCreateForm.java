package com.roommake.cart.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartCreateForm {

    private int productId;        // 상품 번호
    private int productDetailId;  // 상품상세 번호
    private int amount;           // 상품 수량
}
