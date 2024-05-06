package com.roommake.product.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private int id;
    private String name;
    private String content;
    private String statusYn;
    private Date createDate;
    private Date updateDate;
    private Date deleteDate;
    private String deleteYn;
    private int price;
    private int discount;
    private int categoryId;
    private int parentsCategoryId;
    private int parentsId;
    private String imageName;
    private int tagCategoryId;
    private String tagCategoryName;
}
