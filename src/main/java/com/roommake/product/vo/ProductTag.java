package com.roommake.product.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductTag {

    private int id;            // 태그 번호
    private String name;       // 태그 이름
    private Product productId; // 상품 번호
}
