package com.roommake.product.mapper;

import com.roommake.admin.management.vo.Qna;
import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.cart.vo.Cart;
import com.roommake.dto.Criteria;
import com.roommake.product.dto.*;
import com.roommake.product.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> getAllProducts();

    List<ProductDto> getProductsByParentsId(int id);

    List<ProductDto> getProductsById(int id);

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

    List<ProductReviewDto> getProductReviewsById(int ProductId);

    int getProductReviewAmountById(int productId);

    int getProductRatingTotalById(int productId);

    List<ProductListDto> getProductsByPage(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("keyword") String keyword, @Param("type") String type);

    int getTotalProducts(@Param("keyword") String keyword, @Param("type") String type);

    void addProductReviewVote(ProductReviewVote productReviewVote);

    ProductReviewVote getProductReviewVoteById(ProductReviewVote productReviewVote);

    ProductReview getProductReviewById(int reviewId);

    void deleteProductReviewVoteById(ProductReviewVote productReviewVote);

    void addCountProductReviewVote(int reviewId);

    void deleteCountProductReviewVote(int reviewId);

    void createQna(Qna qna);

    List<ProductQnaDto> getProductQnasById(int id);

    int getProductByreviewId(ProductReviewVote productReviewVote);

    int getTotalQnaCountByProdId(int id);

    List<ProductQnaDto> getProductQnas(ProdctQnaCriteria prodctQnaCriteria);

    List<ProductDto> getDifferentProduct(ProductCriteria productCriteria);

    int getProductCategoryIdByProductId(int productId);

    List<ProductTagCategory> getTagsByProductId(int productId);
}