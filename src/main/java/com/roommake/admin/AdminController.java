package com.roommake.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@GetMapping("/home")
	public String adminHome() {
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

	@GetMapping("/product")
	public String product() {
		return "admin/product";
	}

	@GetMapping("/order")
	public String order() {
		return "admin/order";
	}

	@GetMapping("/user")
	public String user() {
		return "admin/user/user";
	}

	@GetMapping("/community")
	public String community() {
		return "admin/community/community";
	}

}
