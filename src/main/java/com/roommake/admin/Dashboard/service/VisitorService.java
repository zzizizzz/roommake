package com.roommake.admin.Dashboard.service;

import com.roommake.admin.Dashboard.mapper.DashboardMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitorService {

    private final DashboardMapper dashboardMapper;

    /**
     * 메인 홈으로 방문하는 방문자 ip 기록
     *
     * @param request
     */
    public void addVisitor(HttpServletRequest request) {
        // ip 주소 획득
        try {
            String ipAddress = request.getRemoteAddr();
            dashboardMapper.createVisitor(ipAddress);
        } catch (DataAccessException ex) {
        }
    }

    /**
     * 전날 ip기준 순방문자 수를 반환한다.
     *
     * @return 순 방문자 수
     */
    public int getVisitorCount() {
        return dashboardMapper.getDailyVisitorCount();
    }

    /**
     * 매일 오전 9시 전날 누적 방문자수를 저장한다.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void createVisitorCount() {
        dashboardMapper.createVisitorCount();
        log.info("누적 방문자 데이터 저장");
    }

    /**
     * 누적 방문자수 조회
     *
     * @return
     */
    public int getTotalVisitorCount() {
        return dashboardMapper.getTotalVisitorCount();
    }

    /**
     * ip 확인용 메소드
     *
     * @param request
     * @return
     */
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        log.info("X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info("WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
            log.info("getRemoteAddr : " + ip);
        }
        log.info("Result : IP Address : " + ip);

        return "admin/user/grade";
    }
}
