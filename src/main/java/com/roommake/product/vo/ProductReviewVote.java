package com.roommake.product.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductReviewVote {

    private ProductReview reviewId; // 리뷰 번호
    private User userId;            // 유저 번호
}
