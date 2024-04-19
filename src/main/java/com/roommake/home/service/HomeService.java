package com.roommake.home.service;

import com.roommake.home.mapper.HomeMapper;
import com.roommake.product.vo.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeMapper homeMapper;

    public List<Product> getMonthlyNew() {
        return homeMapper.selectMonthlyNew();
    }
}
