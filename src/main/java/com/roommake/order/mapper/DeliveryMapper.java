package com.roommake.order.mapper;

import com.roommake.order.vo.Delivery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeliveryMapper {

    List<Delivery> getDeliveriesByUserId(int userId);

    void createDelivery(Delivery delivery);

    void deleteDelivery(Delivery delivery);
}
