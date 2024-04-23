package com.roommake.channel.service;

import com.roommake.channel.dto.ChannelDto;
import com.roommake.channel.dto.PostForm;
import com.roommake.channel.mapper.ChannelMapper;
import com.roommake.channel.mapper.PostMapper;
import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.ChannelParticipant;
import com.roommake.channel.vo.ChannelPost1;
import com.roommake.user.vo.User;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${post.upload.save.directory}")
    private String saveDirectory;

    private final PostMapper postMapper;
    private final ChannelMapper channelMapper;

    public ChannelDto getAllPostsByChannelId(int channelId, String email) {

        ChannelDto dto = new ChannelDto();

        Channel channel = channelMapper.getChannelByChannelId(channelId);
        List<ChannelPost1> channelPosts = postMapper.getAllPosts(channelId);

        dto.setChannel(channel);
        dto.setChannelPosts(channelPosts);

        if (email != null) {
            User user = new User();
            user.setId(1);
            ChannelParticipant channelParticipant = new ChannelParticipant();
            channelParticipant.setChannel(channel);
            channelParticipant.setUser(user);
            if (channelMapper.getChannelParticipant(channelParticipant) != null) {
                dto.setParticipant(true);
            }
        }
        return dto;
    }

    public void createPost(int channelId, PostForm postForm) {
        String imageName = FileUtils.upload(postForm.getImageFile(), saveDirectory);
        User user = new User();
        user.setId(6);
        ChannelPost1 post = ChannelPost1.builder()
                .channel(new Channel(channelId))
                .user(user)
                .title(postForm.getTitle())
                .content(postForm.getContent())
                .imageName(imageName)
                .build();
        postMapper.createPost(post);
    }
}
