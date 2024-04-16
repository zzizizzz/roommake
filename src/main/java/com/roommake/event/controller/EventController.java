package com.roommake.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public class EventController {

    // 이벤트 목록
    @GetMapping("/list")
    public String list() {
        return "event/list";
    }
    
    // 이벤트 출석체크 페이지
    @GetMapping("/dailycheck")
    public String dailycheck() {
        return "event/dailycheck";
    }

}
