package com.roommake.product.mapper;

import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> getAllProduct();

    List<ProductTag> getAllProductTag();

    Product getProductById(int id);
}
