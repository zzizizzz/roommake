package com.roommake.admin.order.dto;

public record OrderHistoryResponseDto(
        int id, int productId, int detailId, int price,
        int qty, int status, String productName, String userNickname) {

}
