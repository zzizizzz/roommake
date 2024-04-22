package com.roommake.order.service;

import com.roommake.order.mapper.DeliveryMapper;
import com.roommake.order.vo.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryMapper deliveryMapper;

    public List<Delivery> getAllDeliveries() {
        return deliveryMapper.getAllDeliveries();
    }

    public void createDelivery(Delivery delivery) {
        deliveryMapper.createDelivery(delivery);
    }
}
