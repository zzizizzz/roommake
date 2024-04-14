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

	// 상품리스트
	@GetMapping("/product")
	public String product() {
		return "admin/product/list";
	}
	// 상픔등록
	@GetMapping("/product/insert")
	public String insert(){
		return "admin/product/insert";
	}
	//상품수정폼
	@GetMapping("/product/modify")
	public String modify(){
		return "admin/product/modify";
	}

	//상품 상세정보
	@GetMapping("/product/detail")
	public String detail() {
		return "admin/product/detail";
	}
	//주문내역 리스트
	@GetMapping("/order")
	public String order() {
		return "admin/order/list";
	}
	// 환불리스트
	@GetMapping("order/refund")
	public String refund(){
		return "admin/order/refund";
	}

	//교환페이지
	@GetMapping("order/exchange")
	public String exchange(){
		return "admin/order/exchange";
	}
	//교환 상세페이지
	@GetMapping("/order/exchange_detail")
	public String exchange_detail(){
		return "admin/order/exchange_detail";
	}

	//반품페이지
	@GetMapping("/order/Return")
	public String Return(){
		return "admin/order/Return";
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
