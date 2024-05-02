package com.roommake.community.controller;

import com.roommake.community.dto.CommunityForm;
import com.roommake.community.enums.CommCatEnum;
import com.roommake.community.service.CommunityService;
import com.roommake.community.vo.Community;
import com.roommake.community.vo.CommunityCategory;
import com.roommake.resolver.Login;
import com.roommake.user.security.LoginUser;
import com.roommake.utils.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Operation(summary = "집들이 목록", description = "집들이 목록을 조회한다.")
    @GetMapping("/houseList")
    public String houseList(Model model) {
        List<Community> houseCommList = communityService.getAllCommunitiesByCatId(CommCatEnum.HOUSE.getCatNo());
        model.addAttribute("houseCommList", houseCommList);

        return "community/house-list";
    }

    @Operation(summary = "노하우 목록", description = "노하우 목록을 조회한다.")
    @GetMapping("/knowhowList")
    public String knowhowList(Model model) {
        List<Community> knowhowCommList = communityService.getAllCommunitiesByCatId(CommCatEnum.KNOW_HOW.getCatNo());
        model.addAttribute("knowhowCommList", knowhowCommList);
        return "community/knowhow-list";
    }

    @Operation(summary = "커뮤니티글 등록폼", description = "커뮤니티글 등록폼을 조회한다.")
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model) {
        List<CommunityCategory> commCatList = communityService.getAllCommCategories();
        model.addAttribute("communityForm", new CommunityForm());
        model.addAttribute("commCatList", commCatList);
        return "community/form";
    }

    @Operation(summary = "커뮤니티글 등록", description = "커뮤니티글 등록 후 커뮤니티 리스트로 이동한다.")
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(@Valid CommunityForm communityForm, BindingResult errors, @Login LoginUser loginUser) {
        if (errors.hasErrors()) {
            return "community/form";
        }
        String s3Url = s3Uploader.saveFile(communityForm.getImageFile());
        communityService.createCommunity(communityForm, s3Url, loginUser.getId());
        return "redirect:/community/houseList";
    }

    @Operation(summary = "커뮤니티글 상세", description = "커뮤니티글 상세내용을 조회한다.")
    @GetMapping("/detail/{commId}")
    public String detail(@PathVariable("commId") int commId, Model model) {
        Community community = communityService.getCommunityByCommId(commId);
        model.addAttribute("community", community);
        return "community/detail";
    }
}
