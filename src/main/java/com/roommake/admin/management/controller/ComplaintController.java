package com.roommake.admin.management.controller;

import com.roommake.admin.management.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin/management/complaint")
@Controller
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/confirm")
    public String confirm(@RequestBody List<String> complaintList) {
        for (String compalintTypeAndId : complaintList) {
            complaintService.confirmCompalint(compalintTypeAndId);
        }
        return "admin/management/complaint";
    }
}
