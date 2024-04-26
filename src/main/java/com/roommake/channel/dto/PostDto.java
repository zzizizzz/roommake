package com.roommake.channel.dto;

import com.roommake.channel.vo.ChannelPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private ChannelPost post;
    private boolean isLike;
}
