package com.roommake.product.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDetail {

    private int id;            // 상품 상세번호
    private int uniqueNumber;  // 상품 고유번호
    private String size;       // 상품 크기
    private String color;      // 상품 색상
    private int stock;         // 상품 재고
    private Product productId; // 상품 번호
    private int price;         // 상품 가격
    private int discount;      // 상품 할인율
}
