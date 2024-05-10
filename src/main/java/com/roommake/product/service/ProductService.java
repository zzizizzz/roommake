package com.roommake.product.service;

import com.roommake.admin.management.vo.Qna;
import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.admin.product.form.ProductCreateForm;
import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.vo.Cart;
import com.roommake.dto.ListDto;
import com.roommake.dto.Pagination;
import com.roommake.product.dto.*;
import com.roommake.product.mapper.ProductMapper;
import com.roommake.product.vo.*;
import com.roommake.user.mapper.UserMapper;
import com.roommake.user.vo.User;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class ProductService {

    private String saveDirectory = "C:\\roommake\\src\\main\\resources\\static\\images\\product";

    @Autowired
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    /**
     * 모든상품 리스트를 반환한다.
     *
     * @return 모든상품리스트
     */
    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }

    public ListDto<ProductDto> getProductsByCategoryId(int categoryId, String type, ProductCriteria productCriteria) {

        productCriteria.setProdCategoryId(categoryId);

        int totalProductCount = 0;
        if ("top".equals(type)) {
            totalProductCount = productMapper.getTotalProductsCountByParentsCategoryId(categoryId);
        } else {
            totalProductCount = productMapper.getTotalProductsCountBySubCategoryId(categoryId);
        }

        Pagination pagination = new Pagination(productCriteria.getPage(), totalProductCount, productCriteria.getRows());
        productCriteria.setBegin(pagination.getBegin());
        productCriteria.setEnd(pagination.getEnd());

        List<ProductDto> productList = List.of();
        if ("top".equals(type)) {
            productList = productMapper.getProductsByParentsId(productCriteria);
        } else {
            productList = productMapper.getProductsBySubCategoryId(productCriteria);
        }
        return new ListDto<ProductDto>(productList, pagination);
    }

    public List<ProductTag> getAllProductTags() {
        return productMapper.getAllProductTags();
    }

    public Product getProductById(int id) {
        return productMapper.getProductById(id);
    }

    public List<ProductDetail> getProductDetailById(int id) {
        return productMapper.getProductDetailById(id);
    }

    public List<ProductCategory> getProductMainCategories() {
        return productMapper.getProductMainCategories();
    }

    public List<ProductCategory> getProductSubCategories(int id) {
        return productMapper.getProductSubCategories(id);
    }

    public void createCart(List<CartCreateForm> formList, int userId) {

        for (CartCreateForm x : formList) {
            User user = User.builder().id(userId).build();

            Product product = new Product();
            product.setId(x.getProductId());

            ProductDetail productDetail = new ProductDetail();
            productDetail.setId(x.getProductDetailId());

            Cart cart = new Cart();

            cart.setProduct(product);
            cart.setUser(user);
            cart.setProductDetail(productDetail);
            cart.setAmount(x.getAmount());

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

    public List<ProductImage> getProductImagesById(int id) {
        return productMapper.getProductImages(id);
    }

    public ListDto<ProductReviewDto> getProductReviewsId(ProdctDetailCriteria prodctDetailCriteria) {

        int totatalReviewCount = productMapper.getTotalReviewCountByProdId(prodctDetailCriteria.getProductId());

        Pagination pagination = new Pagination(prodctDetailCriteria.getPage(), totatalReviewCount, prodctDetailCriteria.getRows());

        prodctDetailCriteria.setBegin(pagination.getBegin());
        prodctDetailCriteria.setEnd(pagination.getEnd());

        List<ProductReviewDto> reviewList = productMapper.getProductReviewsByProductId(prodctDetailCriteria);

        ListDto<ProductReviewDto> dto = new ListDto<ProductReviewDto>(reviewList, pagination);
        return dto;
    }

    public int getProductReviewAmountById(int id) {

        return productMapper.getProductReviewAmountById(id);
    }

    // 상품 리뷰평균점수를 확인하는 구문
    public int getProductRatingTotalById(int productId) {

        return productMapper.getProductRatingTotalById(productId);
    }

    public void addProductReviewVote(int reviewId, String userNickname) {
        ProductReview productReview = productMapper.getProductReviewById(reviewId);
        User user = userMapper.getUserByNickname(userNickname);

        ProductReviewVote productReviewVote = new ProductReviewVote();
        productReviewVote.setUser(user);
        productReviewVote.setReview(productReview);

        ProductReviewVote saveData = productMapper.getProductReviewVoteById(productReviewVote);

        if (saveData == null) {
            productMapper.addProductReviewVote(productReviewVote);
            productMapper.addCountProductReviewVote(reviewId);
        } else {
            productMapper.deleteProductReviewVoteById(productReviewVote);
            productMapper.deleteCountProductReviewVote(reviewId);
        }
    }

    public ProductReview getProductReviewById(int id) {

        return productMapper.getProductReviewById(id);
    }

    public void createQna(Qna qna, int userId) {

        User user = User.builder().id(userId).build();

        qna.setUser(user);

        productMapper.createQna(qna);
    }

//    public List<ProductQnaDto> getProductQnasById(int id) {
//
//        return productMapper.getProductQnasById(id);
//    }

    public int getProductByreviewId(int reviewId, String userNickname) {
        ProductReview productReview = productMapper.getProductReviewById(reviewId);
        User user = userMapper.getUserByNickname(userNickname);

        ProductReviewVote productReviewVote = new ProductReviewVote();
        productReviewVote.setUser(user);
        productReviewVote.setReview(productReview);

        return productMapper.getProductByreviewId(productReviewVote);
    }

    public ListDto<ProductQnaDto> getProductsQnaById(ProdctDetailCriteria prodctDetailCriteria) {

        int totalQnaCount = productMapper.getTotalQnaCountByProdId(prodctDetailCriteria.getProductId());

        Pagination pagination = new Pagination(prodctDetailCriteria.getPage(), totalQnaCount, prodctDetailCriteria.getRows());

        prodctDetailCriteria.setBegin(pagination.getBegin());
        prodctDetailCriteria.setEnd(pagination.getEnd());

        List<ProductQnaDto> qnaList = productMapper.getProductQnas(prodctDetailCriteria);

        ListDto<ProductQnaDto> dto = new ListDto<ProductQnaDto>(qnaList, pagination);
        return dto;
    }

    public List<ProductDto> getDifferentProduct(int productId, ProductCriteria productCriteria) {

        int categoryId = productMapper.getProductCategoryIdByProductId(productId);

        productCriteria.setProdCategoryId(categoryId);
        productCriteria.setRows(4);
        productCriteria.setBegin(1);
        productCriteria.setEnd(4);

        List<ProductDto> prodList = productMapper.getDifferentProduct(productCriteria);

        return prodList;
    }

    ;
}
