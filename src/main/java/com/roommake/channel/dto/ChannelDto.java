package com.roommake.channel.dto;

import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.ChannelPost1;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChannelDto {
    private Channel channel;
    private List<ChannelPost1> channelPosts;
    private boolean isParticipant;
}
