package com.roommake.product.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductReviewVote {

    private int id;                 // 리뷰추천 번호
    private ProductReview reviewId; // 리뷰 번호
    private User userId;            // 유저 번호
}
