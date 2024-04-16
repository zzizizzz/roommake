package com.roommake.channel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/channel")
public class ChannelController {

    @GetMapping("/list")
    public String list() {
        return "channel/list";
    }

    @GetMapping("/form")
    public String createChannel() {
        return "channel/form";
    }

}
