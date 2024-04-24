package com.roommake.channel.controller;

import com.roommake.channel.dto.ChannelForm;
import com.roommake.channel.dto.ChannelInfoDto;
import com.roommake.channel.service.ChannelService;
import com.roommake.channel.vo.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public String createForm(Model model) {
        model.addAttribute("channelForm", new ChannelForm());

        return "channel/form";
    }

    @Operation(summary = "채널 등록", description = "채널을 추가한다.")
    @PostMapping(path = "/create")
    public String createChannel(@Valid ChannelForm channelForm, BindingResult errors) {
        if (errors.hasErrors()) {
            return "channel/form";
        }
        channelService.createChannel(channelForm);

        return "redirect:/channel/list";
    }

    @GetMapping(path = "/modify/{channelId}")
    public String modifyForm(@PathVariable("channelId") int channelId, Principal principal, Model model) {
        String email = principal != null ? principal.getName() : null;
        Channel channel = channelService.getChannelByChannelId(channelId);

        ChannelForm channelForm = new ChannelForm();
        channelForm.setTitle(channel.getTitle());
        channelForm.setDescription(channel.getDescription());
        if (channel.getImageName() != null) {
            String imageName = channel.getImageName();
            model.addAttribute("imageName", imageName);
        }

        model.addAttribute("channelForm", channelForm);
        model.addAttribute("channelId", channelId);

        return "channel/form";
    }

    @PostMapping(path = "/modify/{channelId}")
    public String modifyChannel(@PathVariable("channelId") int channelId, @Valid ChannelForm channelForm, BindingResult errors, Principal principal) {
        if (errors.hasErrors()) {
            return "channel/form";
        }
        String email = principal != null ? principal.getName() : null;
        Channel channel = channelService.getChannelByChannelId(channelId);
        channelService.modifyChannel(channelForm, channel);

        return "redirect:/channel/post/list/{channelId}";
    }

    @GetMapping(path = "/delete/{channelId}")
    public String deleteChannel(@PathVariable("channelId") int channelId, Principal principal) {
        String email = principal != null ? principal.getName() : null;
        Channel channel = channelService.getChannelByChannelId(channelId);
        channelService.deleteChannel(channel);

        return "redirect:/channel/list";
    }

    @Operation(summary = "채널 참여", description = "채널 참여자를 추가한다.")
    @PostMapping("/addUser")
    @ResponseBody
    public ResponseEntity<Void> createParticipant(@RequestParam("channelId") int channelId) {
        channelService.createChannelParticipant(channelId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채널 참여취소", description = "채널 참여자를 삭제한다.")
    @GetMapping("/deleteUser")
    @ResponseBody
    public ResponseEntity<Void> deleteParticipant(@RequestParam("channelId") int channelId) {
        channelService.deleteChannelParticipant(channelId);

        return ResponseEntity.ok().build();
    }
}
