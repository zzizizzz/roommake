package com.roommake.order.service;

import com.roommake.cart.dto.CartCreateForm;
import com.roommake.cart.dto.CartItemDto;
import com.roommake.email.service.MailService;
import com.roommake.order.dto.OrderCreateForm;
import com.roommake.order.dto.OrderDto;
import com.roommake.order.dto.OrderItemDto;
import com.roommake.order.mapper.DeliveryMapper;
import com.roommake.order.mapper.OrderMapper;
import com.roommake.order.vo.Delivery;
import com.roommake.order.vo.Order;
import com.roommake.order.vo.OrderItem;
import com.roommake.order.vo.Payment;
import com.roommake.product.mapper.ProductMapper;
import com.roommake.product.vo.Product;
import com.roommake.product.vo.ProductDetail;
import com.roommake.user.enums.PointReasonEnum;
import com.roommake.user.mapper.UserMapper;
import com.roommake.user.vo.User;
import com.roommake.user.vo.UserGrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final DeliveryMapper deliveryMapper;
    private final MailService mailService;
    private final UserMapper userMapper;

    /**
     * 장바구니에 담긴 상품의 정보를 반환한다.
     *
     * @param forms 장바구니 상품의 상품번호, 상품상세번호, 상품수량이 포함된 CartCreateForm 객체 리스트
     * @return 장바구니 상품의 상세한 정보가 포함된 CartItemDto 객체 리스트
     */
    public List<CartItemDto> getProductsByDetailId(List<CartCreateForm> forms) {

        List<CartItemDto> list = new ArrayList<>();
        for (CartCreateForm form : forms) {
            CartItemDto dto = orderMapper.getProductsByDetailId(form);
            // form에서 받아온 상품수량을 dto에 저장
            dto.setAmount(form.getAmount());
            list.add(dto);
        }

        return list;
    }

    /**
     * 로그인한 유저의 기본 배송지를 반환한다.
     *
     * @param userId 유저 번호
     * @return 로그인한 유저의 기본 배송지
     */
    public Delivery getDefaultDeliveryByUserId(int userId) {

        return orderMapper.getDefaultDeliveryByUserId(userId);
    }

    /**
     * 신규 주문 정보가 저장된 orderCreateForm 객체를 전달받아서 주문정보, 주문상세정보, 결제정보를 생성한다.
     *
     * @param orderCreateForm 신규 주문 정보가 포함된 orderCreateForm 객체
     * @param userId          유저 번호
     */
    @Transactional
    public int createOrder(String tid, OrderCreateForm orderCreateForm, int userId) {

        // 시작 시간 측정
        long beforeTime = System.currentTimeMillis();
        User user = userMapper.getUserById(userId);

        // 주문완료 메일 설정
        String to = user.getEmail();                     // 답변 알림 받을 이메일
        String subject = "주문이 완료되었습니다.";           // 메일 발송시 제목
        Map<String, Object> content = new HashMap<>();   // 메일 콘텐츠를 담을 Map 생성
        content.put("title", orderCreateForm.getName()); // html 템플릿에 적용될 콘텐츠 담기

        mailService.sendEmail(to, subject, "order-email", content);   // 메일 발송시 필요한 정보를 전달한다.

        // 1. 주문정보 생성
        Delivery delivery = deliveryMapper.getDeliveryById(orderCreateForm.getDeliveryId());

        Order order = new Order();      // orderId 없는 상태
        order.setUser(user);
        order.setTotalPrice(orderCreateForm.getTotalPrice());
        order.setDelivery(delivery);
        order.setDeliveryMemo(orderCreateForm.getDeliveryMemo());

        orderMapper.createOrder(order); // orderId 생성

        // 2. 주문상세정보 생성
        List<CartCreateForm> items = orderCreateForm.getItems();
        for (CartCreateForm form : items) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            Product product = productMapper.getProductById(form.getProductId());
            ProductDetail detail = ProductDetail.builder().id(form.getProductDetailId()).build();

            item.setProduct(product);
            item.setProductDetail(detail);
            item.setAmount(form.getAmount());
            item.setPrice(form.getItemPrice());

            orderMapper.createOrderItem(item);
        }

        // 3. 결제정보 생성
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPrice(order.getTotalPrice());
        payment.setUsePoint(orderCreateForm.getUsePoint());
        payment.setTid(tid);

        orderMapper.createPayment(payment);

        // 실행시간을 보기 위해 log 출력
        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        // 메일발송 및 주문완료에 걸린 시간 조회
        log.info("주문 완료 후 메일발송 총 실행 시간: " + diffTime + "ms");

        return order.getId();
    }

    /**
     * 주문정보, 주문상세정보, 결제정보, 배송지정보를 반환한다.
     *
     * @param orderId 주문번호
     * @return 주문정보, 결제정보, 배송지정보, 주문상세정보가 담긴 OrderDto 객체
     */
    public OrderDto getOrderById(int orderId) {

        Payment payment = orderMapper.getPaymentByOrderId(orderId);
        Delivery delivery = orderMapper.getDeliveryByOrderId(orderId);
        List<OrderItemDto> items = orderMapper.getItemsByOrderId(orderId);

        OrderDto orderDto = orderMapper.getOrderById(orderId);
        orderDto.setPayment(payment);
        orderDto.setDelivery(delivery);
        orderDto.setItems(items);
        orderDto.setDeliveryMemo(orderDto.getDeliveryMemo());

        return orderDto;
    }

    /**
     * 주문 아이템의 주문상태를 구매확정으로 변경한다.
     *
     * @param orderItemId 주문상세번호
     */
    @Transactional
    public void confirmOrderItemById(int orderItemId, int orderPrice, int userId) {

        User user = userMapper.getUserById(userId);

        int gradeId = user.getGradeId().getId();                   // 회원등급번호
        UserGrade grade = orderMapper.getUserGradeById(gradeId);   // 회원등급 객체
        int pointRate = grade.getPointRate();                      // 회원등급별 적립률
        int point = (int) (orderPrice * (pointRate / 100.0));      // 등급별 적립률과 주문금액을 고려한 실제 적립 포인트

        String reason = PointReasonEnum.CONFIRM_ORDER.getReason(); // 적립 상세사유 고정문구
        String pointReason = reason + orderItemId;                 // 적립 상세사유 고정문구 + 주문상세 id

        orderMapper.confirmOrderItemById(orderItemId);
        orderMapper.createConfirmOrderPointHistory(point, userId, 7, pointReason);
        orderMapper.addConfirmOrderPointToUser(point, userId);
    }
}
