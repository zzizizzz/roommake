package com.roommake.channel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/channel/post")
public class ChannelPostController {

    @GetMapping("/list")
    public String postList() {
        return "channel/post/list";
    }

    @GetMapping("/form")
    public String createPost() {
        return "channel/post/form";
    }

    @GetMapping("/detail")
    public String detailPost() {
        return "channel/post/detail";
    }

}
