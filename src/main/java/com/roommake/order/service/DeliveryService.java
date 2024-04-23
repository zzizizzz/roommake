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

    public void createDelivery(DeliveryForm form) {
        User user = new User();
        user.setId(4);

        Delivery delivery = Delivery.builder()
                .user(user)
                .name(form.getName())
                .recipient(form.getRecipient())
                .phone(form.getPhone())
                .address1(form.getAddress1())
                .address2(form.getAddress2())
                .zipcode(form.getZipcode())
                .defaultYn(form.getDefaultYn() == null ? "N" : form.getDefaultYn())
                .build();
        deliveryMapper.createDelivery(delivery);
    }

    public void deleteDelivery(int deliveryId) {
        deliveryMapper.deleteDelivery(deliveryId);
    }

    public Delivery getDeliveryById(int deliveryId) {
        return deliveryMapper.getDeliveryById(deliveryId);
    }

    public void modifyDelivery(int deliveryId, DeliveryForm form) {
        Delivery delivery = deliveryMapper.getDeliveryById(deliveryId);

        delivery.setName(form.getName());
        delivery.setRecipient(form.getRecipient());
        delivery.setPhone(form.getPhone());
        delivery.setZipcode(form.getZipcode());
        delivery.setAddress1(form.getAddress1());
        delivery.setAddress2(form.getAddress2());
        delivery.setDefaultYn(form.getDefaultYn() == null ? "N" : form.getDefaultYn());
        deliveryMapper.modifyDelivery(delivery);
    }
}
