package com.roommake.cart.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartItemDto {

    private int cartId;           // 장바구니 번호
    private int amount;           // 장바구니 상품 수량
    private int productId;        // 상품 번호
    private String name;          // 상품 이름
    private int price;            // 상품 가격
    private int discount;         // 상품 할인율
    private int parentsProductId; // 추가상품 번호
    private int productDetailId;  // 상품상세 번호
    private String size;          // 상품상세 크기
    private String color;         // 상품상세 색상
    private int stock;            // 상품상세 재고
}
