package com.roommake.product.mapper;

import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.cart.vo.Cart;
import com.roommake.product.dto.ProductReviewDto;
import com.roommake.product.vo.*;
import com.roommake.user.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> getAllProducts();

    List<Product> getProductsById(int id);

    List<ProductTag> getAllProductTags();

    Product getProductById(int id);

    List<ProductDetail> getProductDetailById(int id);

    List<ProductCategory> getProductMainCategories();

    List<ProductCategory> getProductSubCategories(int id);

    void createCart(Cart cart);

    void insertProduct(Product product);

    void insertProductImage(ProductImage productImage);

    List<ProductListDto> getProducts();

    void modifyProduct(Product product);

    List<ProductImage> getProductImages(int productId);

    int getCategoryId(@Param("id") int productId);

    void insertProductDetail(ProductDetail productDetail);

    List<ProductReviewDto> getProductReviewById(int ProductId);

    int getProductReviewAmountById(int productId);

    int getProductRatingTotalById(int productId);

    List<ProductListDto> getProductsByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    int getTotalProducts();

    void addProductReviewVote(ProductReviewVote productReviewVote);

    ProductReviewVote getProductReviewVoteById(ProductReviewVote productReviewVote);

    void deleteProductReviewVoteById(ProductReviewVote productReviewVote);

    void addCountProductReviewVote(int reviewId);

    void deleteCountProductReviewVote(int reviewId);
}