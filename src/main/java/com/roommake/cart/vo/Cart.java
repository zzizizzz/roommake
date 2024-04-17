package com.roommake.cart.vo;

import com.roommake.product.vo.Product;
import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Cart {
    private int id;             // 장바구니번호
    private Product productId;  // 상품번호
    private User userId;        // 유저번호
    private int itemCount;      // 카트 상품수량

}
