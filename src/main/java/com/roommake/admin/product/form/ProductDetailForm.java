package com.roommake.admin.product.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailForm {
    private String uniqueId;
    private String size;
    private String color;
    private int stock;
}
