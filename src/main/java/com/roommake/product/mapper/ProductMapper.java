package com.roommake.product.mapper;

import com.roommake.cart.vo.Cart;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductCategory;
import com.roommake.product.vo.ProductDetail;
import com.roommake.product.vo.ProductTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> getAllProducts();

    List<Product> getProductsById(int id);

    List<ProductTag> getAllProductTags();

    Product getProductById(int id);

    ProductDetail getProductDetailById(int id);

    List<ProductDetail> getProductSize(int id);

    List<ProductCategory> getAllProductCategories();

    void createCart(Cart cart);
}
