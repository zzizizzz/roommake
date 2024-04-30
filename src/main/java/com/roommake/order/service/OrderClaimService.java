package com.roommake.order.service;

import com.roommake.order.dto.OrderCancelDto;
import com.roommake.order.dto.OrderCancelForm;
import com.roommake.order.dto.OrderDto;
import com.roommake.order.dto.OrderItemDto;
import com.roommake.order.mapper.OrderClaimMapper;
import com.roommake.order.mapper.OrderMapper;
import com.roommake.order.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderClaimService {

    private final OrderClaimMapper orderClaimMapper;
    private final OrderMapper orderMapper;

    /**
     * 모든 주문취소사유를 반환한다.
     *
     * @return 모든 주문취소사유
     */
    public List<OrderCancelReason> getAllCancelReasons() {

        return orderClaimMapper.getAllCancelReasons();
    }

    /**
     * 주문취소/반품/교환을 신청할 내역에 대한 주문정보, 결제정보, 배송지정보, 주문상세정보를 반환한다.
     *
     * @param orderId     주문번호
     * @param orderItemId 주문상세번호
     * @return 주문정보, 결제정보, 배송지정보, 주문상세정보가 담긴 OrderDto 객체
     */
    public OrderDto getOrderClaimByOrderId(int orderId, int orderItemId) {

        Payment payment = orderMapper.getPaymentByOrderId(orderId);
        Delivery delivery = orderMapper.getDeliveryByOrderId(orderId);
        OrderItemDto item = orderClaimMapper.getItemByOrderItemId(orderItemId);

        OrderDto orderDto = orderMapper.getOrderById(orderId);
        orderDto.setPayment(payment);
        orderDto.setDelivery(delivery);
        orderDto.setItem(item);

        return orderDto;
    }

    /**
     * 신규 주문취소 정보가 저장된 orderCancelForm 객체를 전달받아서 주문취소정보, 환불정보 생성 및 주문상태를 갱신한다.
     *
     * @param orderCancelForm 신규 주문취소 정보가 포함된 orderCancelForm 객체
     */
    @Transactional
    public void CreateOrderCancel(OrderCancelForm orderCancelForm) {

        // 1-1. OrderCancel 객체에 저장할 Order 객체 획득
        Order order = new Order();
        order.setId(orderCancelForm.getOrderId());

        // 1-2. 주문취소정보 생성
        OrderCancel orderCancel = new OrderCancel();
        orderCancel.setOrder(order);

        orderClaimMapper.createOrderCancel(order);

        // 2-1. Refund 객체에 저장할 Payment 객체 획득
        Payment payment = new Payment();
        payment.setId(orderCancelForm.getPaymentId());

        // 2-2. 환불정보 생성
        Refund refund = new Refund();
        refund.setAmount(orderCancelForm.getTotalPrice());
        refund.setPayment(payment);

        orderClaimMapper.createCancelRefund(refund);

        // 3. 주문상태 컬럼 갱신
        orderClaimMapper.updateOrderStatus(orderCancelForm.getOrderId());
        orderClaimMapper.updateOrderItemStatus(orderCancelForm.getOrderId());
    }

    /**
     * 주문취소정보, 결제정보, 환불정보, 주문상세정보를 반환한다.
     *
     * @param orderId 주문번호
     * @return 주문취소정보, 결제정보, 환불정보, 주문상세정보가 담긴 OrderCancelDto 객체
     */
    public OrderCancelDto getOrderCancelByOrderId(int orderId) {

        Payment payment = orderMapper.getPaymentByOrderId(orderId);
        Refund refund = orderClaimMapper.getRefundByPaymentId(payment.getId());
        List<OrderItemDto> items = orderMapper.getItemsByOrderId(orderId);

        OrderCancelDto orderCancelDto = orderClaimMapper.getOrderCancelByOrderId(orderId);
        OrderCancelReason Reason = orderClaimMapper.getCancelReasonByCancelId(orderCancelDto.getOrderCancelId());

        orderCancelDto.setPayment(payment);
        orderCancelDto.setRefund(refund);
        orderCancelDto.setReason(Reason);
        orderCancelDto.setItems(items);

        return orderCancelDto;
    }
}
