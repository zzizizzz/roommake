package com.roommake.product.service;

import com.roommake.admin.management.vo.Qna;
import com.roommake.admin.product.dto.ProductListDto;
import com.roommake.admin.product.form.ProductCreateForm;
import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.vo.Cart;
import com.roommake.dto.Criteria;
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

    public List<ProductDto> getProductsByParentsId(int id) {

//        List<ProductDto> productDto = productMapper.getProductsByParentsId(id);
//
//        Map<Integer, List<ProductDto>> productList = productDto.stream().collect(groupingBy(productDto::getId));

        return productMapper.getProductsByParentsId(id);
    }

    public List<ProductDto> getProductsById(int id) {


        return productMapper.getProductsById(id);
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

    public List<ProductReviewDto> getProductReviewsId(int id) {

        return productMapper.getProductReviewsById(id);
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

    public ListDto<ProductQnaDto> getProductsQnaById(ProdctQnaCriteria prodctQnaCriteria) {

        int totalQnaCount = productMapper.getTotalQnaCountByProdId(prodctQnaCriteria.getProductId());

        Pagination pagination = new Pagination(prodctQnaCriteria.getPage(), totalQnaCount, prodctQnaCriteria.getRows());

        prodctQnaCriteria.setBegin(pagination.getBegin());
        prodctQnaCriteria.setEnd(pagination.getEnd());


        List<ProductQnaDto> qnaList = productMapper.getProductQnas(prodctQnaCriteria);

        ListDto<ProductQnaDto> dto = new ListDto<ProductQnaDto>(qnaList, pagination);
        return dto;
    }

    public List<ProductDto> getDifferentProduct(int id, ProductCriteria productCriteria) {

       int categoryId =  productMapper.getProductCategoryIdByProductId(id);

        productCriteria.setProdCategoryId(categoryId);
        productCriteria.setRows(4);
        productCriteria.setBegin(1);
        productCriteria.setEnd(4);

        List<ProductDto> prodList = productMapper.getDifferentProduct(productCriteria);

        return prodList;
    }

    ;
}
