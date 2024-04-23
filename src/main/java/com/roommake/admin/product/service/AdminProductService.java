package com.roommake.admin.product.service;

import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.admin.product.form.ProductCreateForm;
import com.roommake.product.mapper.ProductMapper;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductCategory;
import com.roommake.product.vo.ProductImage;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductMapper productMapper;

    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }

    private String saveDirectory = "C:\\roommake\\src\\main\\resources\\static\\images\\product";

    public void insertProduct(ProductCreateForm form) {
        Product product = new Product();
        product.setName(form.getName());
        product.setPrice(form.getPrice());
        product.setDiscount(form.getDiscount());
        product.setContent(form.getContent());

        ProductCategory category = new ProductCategory();
        category.setId(form.getCategoryId());
        product.setCategory(category);

        productMapper.insertProduct(product);

        String filename = FileUtils.upload(form.getImageFile(), saveDirectory);
        ProductImage productImage = new ProductImage();
        productImage.setProductId(product);
        productImage.setName(filename);
        productMapper.insertProductImage(productImage);
    }

    public List<ProductListDto> getProducts() {
        return productMapper.getProducts();
    }

    public Product getProductDetail(int id) {
        return productMapper.getProductById(id);
    }
}
