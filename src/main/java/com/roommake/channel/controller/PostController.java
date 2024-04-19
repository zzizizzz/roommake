package com.roommake.channel.controller;

import com.roommake.channel.service.ChannelService;
import com.roommake.channel.service.PostService;
import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.Channelpost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/channel/post")
@RequiredArgsConstructor
@Tag(name = "Post API", description = "각 채널의 글에 대한 추가,변경,삭제,조회 API를 제공한다.")
public class PostController {

    private final ChannelService channelService;
    private final PostService postService;

    @Operation(summary = "채널에 대한 전체글 조회", description = "해당 채널에 대한 전체글을 조회한다.")
    @GetMapping("/list/{channelId}")
    public String postList(@PathVariable("channelId") int channelId, Model model) {
        Channel channel = channelService.getChannelById(channelId);
        List<Channelpost> postList = postService.getAllPosts(channelId);
        model.addAttribute("channel", channel);
        model.addAttribute("postList", postList);

        return "channel/post/list";
    }
    
    @GetMapping("/create")
    public String createPost() {
        return "channel/post/form";
    }

    @GetMapping("/detail")
    public String detailPost() {
        return "channel/post/detail";
    }
}
