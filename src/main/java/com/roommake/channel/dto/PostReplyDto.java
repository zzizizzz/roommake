package com.roommake.channel.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PostReplyDto {

    private int totalReplyCount;
    private List<?> postReplies;
}
