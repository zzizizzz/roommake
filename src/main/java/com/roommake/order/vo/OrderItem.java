package com.roommake.order.vo;

import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItem {

    private int id;                        // 주문상세번호
    private Order orderId;                 // 주문번호
    private Product productId;             // 상품번호
    private ProductDetail productDetailId; // 상품상세번호
    private int amount;                    // 상품수량
    private int price;                     // 상품가격
    private String claimYn;                // 취소반품여부
}
