package com.roommake.channel.controller;

import com.roommake.channel.dto.ChannelCreateForm;
import com.roommake.channel.dto.ChannelInfoDto;
import com.roommake.channel.service.ChannelService;
import com.roommake.channel.vo.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<ChannelInfoDto> channelList = channelService.getAllChannels();
        List<Channel> participationChannelList = channelService.getChannelsByUserId(1);
        model.addAttribute("channelList", channelList);
        model.addAttribute("participationChannelList", participationChannelList);

        return "channel/list";
    }

    @Operation(summary = "채널 등록폼", description = "채널 등록폼를 조회한다.")
    @GetMapping(path = "/create")
    public String form() {
        return "channel/form";
    }

    @Operation(summary = "채널 등록", description = "채널정보를 추가한다.")
    @PostMapping(path = "/create")
    public String createChannel(ChannelCreateForm channelCreateForm) {
        channelService.createChannel(channelCreateForm);
        return "redirect:list";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Void> addParticipant(@RequestParam("channelId") int channelId) {
        channelService.createChannelParticipant(channelId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteParticipant(@RequestParam("channelId") int channelId) {
        channelService.deleteChannelParticipant(channelId);
        return ResponseEntity.ok().build();
    }
}
