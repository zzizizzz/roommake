package com.roommake.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/community")
public class CommunityController {

    @GetMapping("/houselist")
    public String houseList() {
        return "community/house-list";
    }

    @GetMapping("/knowhowlist")
    public String knowhowList() {
        return "community/knowhow-list";
    }

    @GetMapping("/form")
    public String create() {
        return "community/form";
    }

    @GetMapping("/detail")
    public String detail() {
        return "community/detail";
    }

}
