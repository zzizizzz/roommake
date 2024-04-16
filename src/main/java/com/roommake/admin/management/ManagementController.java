package com.roommake.admin.management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/management")
@Controller
public class ManagementController {

	@GetMapping("/notice")
	public String notice() {
		return "admin/management/notice";
	}

	@GetMapping("/faq")
	public String faq() {
		return "admin/management/faq";
	}

	@GetMapping("/qna")
	public String qna() {
		return "admin/management/qna";
	}

	@GetMapping("/banner")
	public String banner() {
		return "admin/management/banner";
	}

	@GetMapping("/complaint")
	public String complaint() {
		return "admin/management/complaint";
	}
}
