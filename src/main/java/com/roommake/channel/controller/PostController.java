package com.roommake.channel.controller;

import com.roommake.channel.dto.ChannelDto;
import com.roommake.channel.dto.PostForm;
import com.roommake.channel.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/channel/post")
@RequiredArgsConstructor
@Tag(name = "Post API", description = "각 채널의 글에 대한 추가,변경,삭제,조회 API를 제공한다.")
public class PostController {

    private final PostService postService;

    @Operation(summary = "채널에 대한 전체글 조회", description = "해당 채널에 대한 채널정보, 전체글, 참가여부를 조회한다.")
    @GetMapping("/list/{channelId}")
    public String postList(@PathVariable("channelId") int channelId, Model model, Principal principal) {

        String email = principal != null ? principal.getName() : null;
        ChannelDto dto = postService.getAllPostsByChannelId(channelId, email);

        model.addAttribute("channel", dto.getChannel());
        model.addAttribute("postList", dto.getChannelPosts());
        model.addAttribute("participant", true);

        return "channel/post/list";
    }

    @Operation(summary = "채널글 등록폼", description = "채널글 등록폼를 조회한다.")
    @GetMapping(path = "/create/{channelId}")
    public String createForm(@PathVariable("channelId") int channelId, Model model) {
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("channelId", channelId);

        return "channel/post/form";
    }

    @Operation(summary = "채널글 등록", description = "채널글을 추가한다.")
    @PostMapping(path = "/create/{channelId}")
    public String createChannel(@PathVariable("channelId") int channelId, @Valid PostForm postForm, BindingResult errors) {
        if (errors.hasErrors()) {
            return "channel/post/form";
        }
        postService.createPost(channelId, postForm);

        return "redirect:/channel/post/list/{channelId}";
    }

    @GetMapping("/detail")
    public String detailPost() {

        return "channel/post/detail";
    }
}
