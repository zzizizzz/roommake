package com.roommake.product.mapper;

import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.cart.vo.Cart;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductCategory;
import com.roommake.product.vo.ProductDetail;
import com.roommake.product.vo.ProductImage;
import com.roommake.product.vo.ProductTag;
import com.roommake.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> getAllProducts();

    List<Product> getProductsById(int id);

    List<ProductTag> getAllProductTags();

    Product getProductById(int id);

    List<ProductDetail> getProductDetailById(int id);

    List<ProductCategory> getProductMainCategories();

    List<ProductCategory> getProductSubCategories();

    void createCart(Cart cart);

    void insertProduct(Product product);

    void insertProductImage(ProductImage productImage);

    List<ProductListDto> getProducts();

    void modifyProduct(Product product);
}
