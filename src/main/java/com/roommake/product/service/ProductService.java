package com.roommake.product.service;

import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.admin.product.form.ProductCreateForm;
import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.vo.Cart;
import com.roommake.product.mapper.ProductMapper;
import com.roommake.product.vo.*;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private String saveDirectory = "C:\\roommake\\src\\main\\resources\\static\\images\\product";

    @Autowired
    private final ProductMapper productMapper;

    /**
     * 모든상품 리스트를 반환한다.
     *
     * @return 모든상품리스트
     */
    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }

    public List<ProductTag> getAllProductTags() {
        return productMapper.getAllProductTags();
    }

    public Product getProductById(int id) {
        return productMapper.getProductById(id);
    }

    public ProductDetail getProductDetailById(int id) {
        return productMapper.getProductDetailById(id);
    }

    public List<ProductDetail> getProductSize(int id) {
        return productMapper.getProductSize(id);
    }

    public void createCart(List<CartCreateForm> formList) {

        for (CartCreateForm x : formList) {

            Cart cart = new Cart();
            cart.setProduct(getProductById(x.getId()));
            cart.setProductDetail(getProductDetailById(x.getDetails()));
            cart.setItemCount(x.getAmount());

            productMapper.createCart(cart);
        }
    }

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

        for (MultipartFile mf : form.getImageFiles()) {
            String filename = FileUtils.upload(mf, saveDirectory);
            ProductImage productImage = new ProductImage();
            productImage.setProductId(product);
            productImage.setName(filename);
            productMapper.insertProductImage(productImage);
        }
    }

    public List<ProductListDto> getProducts() {
        return productMapper.getProducts();
    }
}
