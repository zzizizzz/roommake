package com.roommake.admin.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductDetailDto {
    private int id;
    private String name;
    private int price;
    private Date createDate;
    private String content;
    private String imageName;
    private int categoryId;
    private String categoryName;
}
