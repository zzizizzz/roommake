package com.roommake.cart.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartCreateForm {

    private int id;
    private int details;
    private int amount;
}
