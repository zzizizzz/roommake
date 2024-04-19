package com.roommake.channel.controller;

import com.roommake.channel.dto.ChannelCreateForm;
import com.roommake.channel.service.ChannelService;
import com.roommake.channel.vo.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
@Tag(name = "Channel API", description = "채널정보 추가,변경,삭제,조회 API를 제공한다.")
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "전체 채널 조회", description = "전체 채널정보를 조회한다.")
    @GetMapping("/list")
    public String list(Model model) {
        List<Channel> channelList = channelService.getAllChannel();
        model.addAttribute("channelList", channelList);

        return "channel/list";
    }

    @Operation(summary = "채널 등록폼", description = "채널 등록폼를 조회한다.")
    @GetMapping(path = "/create")
    public String form() {
        return "channel/form";
    }

    @Operation(summary = "채널 추가", description = "채널정보를 추가한다.")
    @PostMapping(path = "/create")
    public String createChannel(ChannelCreateForm channelCreateForm) {
        channelService.createChannel(channelCreateForm);
        return "redirect:list";
    }
}
