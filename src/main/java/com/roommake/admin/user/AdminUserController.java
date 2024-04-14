package com.roommake.admin.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    @GetMapping("/userList")
    public String userList() {
        return "admin/user/user";
    }

    @GetMapping("/point")
    public String point() {
        return "admin/user/point";
    }

    @GetMapping("/grade")
    public String grade() {
        return "admin/user/grade";
    }
}
