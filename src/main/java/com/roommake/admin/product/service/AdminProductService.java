package com.roommake.admin.product.service;

import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.admin.product.form.ProductCreateForm;
import com.roommake.admin.product.form.ProductDetailForm;
import com.roommake.product.mapper.ProductMapper;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductCategory;
import com.roommake.product.vo.ProductDetail;
import com.roommake.product.vo.ProductImage;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        String imageName = "";
        ProductImage productImage = new ProductImage();
        if (!form.getImageFiles().isEmpty()) {
            for (MultipartFile multipartFile : form.getImageFiles()) {
                String filename = FileUtils.upload(multipartFile, saveDirectory);
                productImage.setProductId(product);
                productImage.setName(filename);
                productMapper.insertProductImage(productImage);
            }
        } else {
            productImage.setProductId(product);
            productImage.setName("default.jpg");
            productMapper.insertProductImage(productImage);
        }
    }

    public List<ProductListDto> getProducts() {
        return productMapper.getProducts();
    }

    public void modifyProduct(Product product) {
        productMapper.modifyProduct(product);
    }

    public void insertProductDetail(ProductDetailForm productDetailForm) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setUniqueId(productDetail.getUniqueId());
        productDetail.setSize(productDetailForm.getSize());
        productDetail.setColor(productDetailForm.getColor());
        productDetail.setStock(productDetailForm.getStock());
        productMapper.insertProductDetail(productDetail);
    }
}

