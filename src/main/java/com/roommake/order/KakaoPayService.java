package com.roommake.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoPayService {

    public ReadyResponse payReady(int quantity, int totalPrice) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
        parameters.put("partner_order_id", "1234567890");                       // 주문번호
        parameters.put("partner_user_id", "roommake");                          // 회원 아이디
        parameters.put("item_name", "조명");                                     // 상품명
        parameters.put("quantity", "1");                                        // 상품 수량
        parameters.put("total_amount", "20000");                                // 상품 총액
        parameters.put("tax_free_amount", "100");                               // 상품 비과세 금액
        parameters.put("approval_url", "http://localhost/order/pay/completed"); // 결제 성공 시 URL
        parameters.put("cancel_url", "http://localhost/order/pay/cancel");      // 결제 취소 시 URL
        parameters.put("fail_url", "http://localhost/order/pay/fail");          // 결제 실패 시 URL

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        ResponseEntity<ReadyResponse> responseEntity = template.postForEntity(url, requestEntity, ReadyResponse.class);
        log.info("결제준비 응답객체: " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    public ApproveResponse payApprove(String tid, String pgToken) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
        parameters.add("tid", tid);                                             // 결제 고유번호
        parameters.add("partner_order_id", "1234567890");                       // 주문번호
        parameters.add("partner_user_id", "roommake");                          // 회원 아이디
        parameters.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
        log.info("결제승인 응답객체: " + approveResponse);

        return approveResponse;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY DEVAF817C1D3C91B1D1D297C121A203952984CF7");
        headers.set("Content-type", "application/json");

        return headers;
    }
}
