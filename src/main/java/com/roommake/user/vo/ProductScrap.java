package com.roommake.user.vo;

import com.roommake.product.vo.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ProductScrap {

    private int id;                    // 상품 스크랩 번호
    private Date createDate;           // 상품 스크랩 날짜
    private int count;                 // 상품 스크랩 수
    private User userId;               // 유저 번호
    private Product productId;         // 상품 번호
    private ScrapFolder scrapFolderId; // 스크랩 폴더 번호
}
