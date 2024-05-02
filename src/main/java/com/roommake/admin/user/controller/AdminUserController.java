package com.roommake.admin.user.controller;

import com.roommake.dto.Criteria;
import com.roommake.dto.ListDto;
import com.roommake.user.service.UserService;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/userList")
    public String userList(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
                           @RequestParam(name = "sort", required = false, defaultValue = "date") String sort,
                           @RequestParam(name = "filt", required = false, defaultValue = "total") String filt,
                           @RequestParam(name = "opt", required = false) String opt,
                           @RequestParam(name = "keyword", required = false) String keyword,
                           Model model) {
        
        Criteria criteria = new Criteria();
        criteria.setPage(page);
        criteria.setRows(rows);
        criteria.setSort(sort);
        criteria.setFilt(filt);

        if (StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
            criteria.setOpt(opt);
            criteria.setKeyword(keyword);
        }

        ListDto<User> dto = userService.getUsers(criteria);

        model.addAttribute("criteria", criteria);
        model.addAttribute("paging", dto.getPaging());
        model.addAttribute("users", dto.getItems());

        return "admin/user/user-list";
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
