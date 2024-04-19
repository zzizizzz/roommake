package com.roommake.cart.mapper;

import com.roommake.product.vo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Product> getNewProducts();
}
