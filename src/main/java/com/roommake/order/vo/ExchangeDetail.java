package com.roommake.order.vo;

import com.roommake.product.vo.ProductDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExchangeDetail {

    private int id;                                 // 교환상세번호
    private Exchange exchangeId;                    // 교환번호
    private ProductDetail BeforeProductDetailId;    // 교환전 상품상세번호
    private ProductDetail AfterProductDetailId;     // 교환후 상품상세번호

}
