package com.roommake.channel.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class ChannelCreateForm {

    private String title;
    private String description;
    private MultipartFile imageFile;
}
