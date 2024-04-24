package com.roommake.admin.product.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductCreateForm {
    private String name;
    private int price;
    private int categoryId;
    private int discount;
    private List<MultipartFile> imageFiles;
    private String content;
}
