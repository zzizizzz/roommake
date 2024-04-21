package com.roommake.order.vo;

import com.roommake.user.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Delivery {

    private int id;           // 배송지 번호
    private User user;        // 유저 번호
    private String name;      // 배송지 이름
    private String recipient; // 수령인
    private String phone;     // 연락처
    private String address1;  // 주소1
    private String address2;  // 주소2
    private String zipcode;   // 우편번호
    private String memo;      // 배송메모
    private String defaultYn; // 기본배송지 여부
}
