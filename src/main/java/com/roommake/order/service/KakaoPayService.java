package com.roommake.order.service;

import com.roommake.order.dto.ApproveResponse;
import com.roommake.order.dto.CancelResponse;
import com.roommake.order.dto.ReadyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KakaoPayService {

    /**
     * 카카오페이 결제창 연결
     *
     * @param quantity   상품 수량
     * @param totalPrice 상품 총액
     * @return 결제준비 응답객체
     */
    public ReadyResponse payReady(int quantity, int totalPrice) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");                                    // 가맹점 코드(테스트용)
        parameters.put("partner_order_id", "1234567890");                       // 주문번호
        parameters.put("partner_user_id", "roommake");                          // 회원 아이디
        parameters.put("item_name", "테스트으으으");                              // 상품명
        parameters.put("quantity", "1");                                        // 상품 수량
        parameters.put("total_amount", "2200");                                 // 상품 총액
        parameters.put("cancel_available_amount", "2200");                                 // 상품 총액
        parameters.put("tax_free_amount", "0");                                 // 상품 비과세 금액
        parameters.put("approval_url", "http://localhost/order/pay/completed"); // 결제 성공 시 URL
        parameters.put("cancel_url", "http://localhost/order/pay/cancel");      // 결제 취소 시 URL
        parameters.put("fail_url", "http://localhost/order/pay/fail");          // 결제 실패 시 URL

        // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // RestTemplate
        // : Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        //   REST API 호출 이후 응답을 받을 때까지 기다리는 동기 방식 (json, xml 응답)
        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드
        ResponseEntity<ReadyResponse> responseEntity = template.postForEntity(url, requestEntity, ReadyResponse.class);
        log.info("결제준비 응답객체: " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    /**
     * 카카오페이 결제 승인
     *
     * @param tid     결제 고유번호
     * @param pgToken 결제승인 요청을 인증하는 토큰
     * @return 결제승인 응답객체
     */
    public ApproveResponse payApprove(String tid, String pgToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
        parameters.put("tid", tid);                       // 결제 고유번호
        parameters.put("partner_order_id", "1234567890"); // 주문번호
        parameters.put("partner_user_id", "roommake");    // 회원 아이디
        parameters.put("pg_token", pgToken);              // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
        log.info("결제승인 응답객체: " + approveResponse);

        return approveResponse;
    }

    /**
     * 카카오페이 결제 취소
     *
     * @param tid 결제 고유번호
     * @return 취소승인 응답객체
     */
    public CancelResponse payCancel(String tid) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");               // 가맹점 코드(테스트용)
        parameters.put("tid", tid);                        // 결제 고유번호
        parameters.put("cancel_amount", "2200");           // 취소 금액
        parameters.put("cancel_tax_free_amount", "0");     // 취소 비과세 금액

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/cancel";
        ResponseEntity<CancelResponse> responseEntity = template.postForEntity(url, requestEntity, CancelResponse.class);
        log.info("취소승인 응답객체: " + responseEntity.getBody());

        return responseEntity.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY DEVAF817C1D3C91B1D1D297C121A203952984CF7");
        headers.set("Content-type", "application/json");

        return headers;
    }
}
