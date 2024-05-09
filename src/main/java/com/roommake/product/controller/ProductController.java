package com.roommake.product.controller;

import com.roommake.admin.management.service.QnaService;
import com.roommake.admin.management.vo.Qna;
import com.roommake.admin.management.vo.QnaCategory;
import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.dto.CartItemDto;
import com.roommake.cart.dto.CartListDto;
import com.roommake.cart.service.CartService;
import com.roommake.dto.Criteria;
import com.roommake.dto.ListDto;
import com.roommake.product.dto.*;
import com.roommake.product.service.ProductService;
import com.roommake.product.vo.*;
import com.roommake.resolver.Login;
import com.roommake.user.security.LoginUser;
import com.roommake.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "상품들의 조회 API를 제공한다.")
public class ProductController {

    private final ProductService productService;
    private final QnaService qnaService;
    private final CartService cartService;

    // 상품홈으로 이동하는 메소드
    @GetMapping("/home")
    public String store(Model model) {

        return "store/home";
    }

    // 상품디테일로 이동하는 메소드
    @Operation(summary = "해당상품 상세 정보조회", description = "해당 상품의 정보를 조회한다")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id,
                         @RequestParam(name = "page", required = false, defaultValue = "1") int CurrentPage,
                         @RequestParam(name = "rows", required = false, defaultValue = "5") int rows,
                         Model model) {
        ProdctQnaCriteria prodctQnaCriteria = new ProdctQnaCriteria();
        prodctQnaCriteria.setPage(CurrentPage);
        prodctQnaCriteria.setProductId(id);
        prodctQnaCriteria.setRows(rows);

        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setPage(CurrentPage);

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        List<ProductDetail> productDetail = productService.getProductDetailById(id);
        model.addAttribute("productDetail", productDetail);

        List<ProductReviewDto> productReviews = productService.getProductReviewsId(id);
        model.addAttribute("productReviews", productReviews);

        int productReviewAmount = productService.getProductReviewAmountById(id);
        model.addAttribute("productReviewAmount", productReviewAmount);

        double productRatingTotal = productService.getProductRatingTotalById(id);
        model.addAttribute("productRatingTotal", productRatingTotal);

        List<QnaCategory> qnaCategories = qnaService.getQnaCategories();
        model.addAttribute("qnaCategories", qnaCategories);

        ListDto<ProductQnaDto> dto = productService.getProductsQnaById(prodctQnaCriteria);
        model.addAttribute("qnas", dto.getItems());
        model.addAttribute("paging", dto.getPaging());

        List<ProductDto> productDifferentList = productService.getDifferentProduct(id, productCriteria);
        model.addAttribute("productDifferentList", productDifferentList);

        return "store/product-detail";
    }

    /**
     * 상품리스트를 불러오는 메소드
     *
     * @param model
     * @return 전체 상품리스트
     */
    @GetMapping("/category/{id}/{type}")
    public String list(@PathVariable int id,
                       @PathVariable String type,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int CurrentPage,
                       @RequestParam(name = "rows", required = false, defaultValue = "28") int rows,
                       Model model) {
        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setPage(CurrentPage);
        productCriteria.setRows(rows);

        ListDto<ProductDto> products = productService.getProductsByCategoryId(id, type, productCriteria);
        model.addAttribute("products", products.getItems());
        model.addAttribute("paging", products.getPaging());
        model.addAttribute("id", id);
        model.addAttribute("type", type);

        List<ProductCategory> productCategories = productService.getProductMainCategories();

        return "store/category-list";
    }

    @PostMapping("/addCart")
    @PreAuthorize("isAuthenticated()")
    public String addCart(@RequestParam("id") int id,
                          @RequestParam("productDetailId") List<Integer> details,
                          @RequestParam("amount") List<Integer> amounts,
                          @Login LoginUser loginUser,
                          Model model) {

        List<CartCreateForm> cartFormList = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            CartCreateForm form = new CartCreateForm();
            form.setProductId(id);
            form.setProductDetailId(details.get(i));
            form.setAmount(amounts.get(i));

            cartFormList.add(form);
        }
        productService.createCart(cartFormList, loginUser.getId());
        List<CartItemDto> items = cartService.getCartsByUserId(loginUser.getId());
        CartListDto dto = new CartListDto(items);

        model.addAttribute("dto", dto);

        return "cart/cart";
    }

    @GetMapping("/replyVote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String replyVote(@PathVariable int id, @Login LoginUser loginuser) {

        String userNickname = loginuser.getNickname();
        productService.addProductReviewVote(id, userNickname);
        int productId = productService.getProductByreviewId(id, userNickname);

        return String.format("redirect:/store/detail/%d", productId);
    }

    @PostMapping("/qna/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createQna(@PathVariable int id,
                            @RequestParam("categoryId") int categoryId,
                            @RequestParam("title") String title,
                            @RequestParam(name = "secret", required = false, defaultValue = "N") String secret,
                            @RequestParam("content") String content,
                            @Login LoginUser loginuser) {

        Product product = productService.getProductById(id);
        QnaCategory qnaCategory = qnaService.getQnaCategory(categoryId);

        System.out.println(secret);

        Qna qna = new Qna();
        qna.setProduct(product);
        qna.setTitle(title);
        qna.setContent(content);
        qna.setCategory(qnaCategory);
        qna.setPrivateYn(secret);

        productService.createQna(qna, loginuser.getId());

        return String.format("redirect:/store/detail/%d", id);
    }

//    @PostMapping("/replyCreate/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public String creatReply(@PathVariable int id, @RequestParam("reviewStar") int reviewStar, @RequestParam("content") String content, @Login LoginUser loginuser) {
//
//        ProductReviewDto productReviewDto = new ProductReviewDto();
//        productReviewDto.setReviewStar(reviewStar);
//        productReviewDto.setContent(content);
//
//        ProductReview productReview = productService.creatReply(id, productReviewDto, loginuser.getId());
//
//        return null;
//    }

    @GetMapping("/category")
    @ResponseBody
    public List<ProductCategory> subcategory(@RequestParam("id") int productId) {

        return productService.getProductSubCategories(productId);
    }

    // 스크랩 popup으로 이동하는 메소드
    @GetMapping("/popup")
    public String popup() {
        return "layout/scrap-popup";
    }
}
