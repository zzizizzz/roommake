package com.roommake.admin;

import com.roommake.admin.Dashboard.dto.DashboardDto;
import com.roommake.admin.Dashboard.service.DashBoardService;
import com.roommake.admin.product.service.AdminProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "관리자 API", description = "관리자 세부 페이지로 가는 CRUD API를 제공한다.")
public class AdminController {
    private final AdminProductService adminProductService;
    private final DashBoardService dashBoardService;

    @GetMapping("/home")
    public String adminHome(Model model) {

        DashboardDto dto = dashBoardService.getAdminHomeDto();

        model.addAttribute("salesDataList", dto.getSalesDataList());
        model.addAttribute("noAnswerQnas", dto.getNoAnswerQnas());
        model.addAttribute("newUserCnt", dto.getNewUserCnt());
        model.addAttribute("totalUserCnt", dto.getTotalUserCnt());
        System.out.println(dto.getSalesDataList());
        return "admin/home";
    }

    @GetMapping("/management")
    public String notice() {
        return "admin/management/notice";
    }

    @GetMapping("/sales")
    public String sales() {
        return "admin/sales/sales";
    }

    //주문내역 리스트
    @GetMapping("/order/item")
    public String order() {
        return "admin/order/item";
    }

    // 상품리스트
    @GetMapping("/product/list")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        // 페이징 처리
        int pageSize = 10;
        int totalProducts = adminProductService.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        page = Math.min(Math.max(page, 1), totalPages);
        model.addAttribute("products", adminProductService.getProductByPage(page, pageSize));
        model.addAttribute("page", Integer.valueOf(page));
        model.addAttribute("totalPages", Integer.valueOf(totalPages));

        return "admin/product/list";
    }

    @GetMapping("/user")
    public String user() {
        return "admin/user/user-list";
    }

    @GetMapping("/community")
    public String community() {
        return "admin/community/community";
    }
}
