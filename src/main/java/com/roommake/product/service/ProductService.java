package com.roommake.product.service;

import com.roommake.cart.vo.Cart;
import com.roommake.cart.dto.CartCrateForm;
import com.roommake.product.mapper.ProductMapper;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductDetail;
import com.roommake.product.vo.ProductTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

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

    public void createCart(List<CartCrateForm> formList) {

        for (CartCrateForm x : formList) {

            Cart cart = new Cart();
            cart.setProduct(getProductById(x.getId()));
            cart.setProductDetail(getProductDetailById(x.getDetails()));
            cart.setItemCount(x.getAmount());

            productMapper.createCart(cart);
        }
    }
}
