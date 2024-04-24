package com.roommake.community.controller;

import com.roommake.community.dto.CommunityForm;
import com.roommake.community.service.CommunityService;
import com.roommake.community.vo.Community;
import com.roommake.community.vo.CommunityCategory;
import com.roommake.utils.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
@Tag(name = "커뮤니티 API", description = "커뮤니티 추가,변경,삭제,조회 API를 제공한다.")
public class CommunityController {

    private final CommunityService communityService;
    private final S3Uploader s3Uploader;

    @Operation(summary = "이미지 업로드", description = "이미지를 서버, S3에 저장한다.")
    @PostMapping("/image/upload")
    @ResponseBody
    public Map<String, Object> imageUpload(MultipartRequest request) {

        Map<String, Object> responseData = new HashMap<>();

        try {
            String s3Url = communityService.imageUpload(request);
            responseData.put("uploaded", true);
            responseData.put("url", s3Url);
            return responseData;
        } catch (IOException e) {
            responseData.put("uploaded", false);
            return responseData;
        }
    }

    @GetMapping("/houselist")
    public String houseList(Model model) {
        List<Community> houseCommList = communityService.getAllCommunitiesByCatId(1);
        model.addAttribute("houseCommList", houseCommList);

        return "community/house-list";
    }

    @GetMapping("/knowhowlist")
    public String knowhowList() {
        return "community/knowhow-list";
    }

    @Operation(summary = "커뮤니티글 작성폼", description = "커뮤니티글 작성폼을 조회한다.")
    @GetMapping("/create")
    public String create(Model model) {
        List<CommunityCategory> commCatList = communityService.getAllCommCategories();
        model.addAttribute("communityForm", new CommunityForm());
        model.addAttribute("commCatList", commCatList);
        return "community/form";
    }

    @Operation(summary = "커뮤니티글 등록", description = "커뮤니티글 등록 후 커뮤니티 리스트로 이동한다.")
    @PostMapping("/create")
    public String create(@Valid CommunityForm communityForm, BindingResult errors) {
        if (errors.hasErrors()) {
            return "community/form";
        }
        String s3Url = s3Uploader.saveFile(communityForm.getImageFile());
        communityService.createCommunity(communityForm, s3Url);
        return "redirect:/community/houselist";
    }

    @Operation(summary = "커뮤니티글 상세", description = "커뮤니티글 상세내용을 조회한다.")
    @GetMapping("/detail/{commId}")
    public String detail(@PathVariable("commId") int commId, Model model) {
        Community community = communityService.getCommunityByCommId(commId);
        model.addAttribute("community", community);
        return "community/detail";
    }
}
