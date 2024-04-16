package com.roommake.admin.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/community")
public class AdminCommController {

    @GetMapping("commList")
    public String commList() {
        return "admin/community/community";
    }

    @GetMapping("channel")
    public String channelList() {
        return "admin/community/channel";
    }
}
