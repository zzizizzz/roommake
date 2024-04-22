package com.roommake.order.service;

import com.roommake.order.dto.DeliveryForm;
import com.roommake.order.mapper.DeliveryMapper;
import com.roommake.order.vo.Delivery;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryMapper deliveryMapper;

    public List<Delivery> getDeliveriesByUserId(int userId) {
        return deliveryMapper.getDeliveriesByUserId(userId);
    }

    public void createDelivery(DeliveryForm deliveryForm) {
        User user = new User();
        user.setId(4);

        Delivery delivery = Delivery.builder()
                .user(user)
                .name(deliveryForm.getName())
                .recipient(deliveryForm.getRecipient())
                .phone(deliveryForm.getPhone())
                .address1(deliveryForm.getAddress1())
                .address2(deliveryForm.getAddress2())
                .zipcode(deliveryForm.getZipcode())
                .defaultYn(deliveryForm.getDefaultYn())
                .build();
        deliveryMapper.createDelivery(delivery);
    }

    public void deleteDelivery(int deliveryId) {
        Delivery delivery = new Delivery();
        delivery.setId(deliveryId);
        delivery.toDelivery(4);
        deliveryMapper.deleteDelivery(delivery);
    }
}
