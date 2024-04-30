package com.roommake.order.mapper;

import com.roommake.order.vo.OrderCancelReason;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderClaimMapper {

    List<OrderCancelReason> getAllCancelReasons();
}
