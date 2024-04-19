package com.roommake.home.mapper;

import com.roommake.product.vo.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HomeMapper {

    List<Product> selectMonthlyNew();
}
