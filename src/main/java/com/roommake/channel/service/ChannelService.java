package com.roommake.channel.service;

import com.roommake.channel.dto.ChannelForm;
import com.roommake.channel.dto.ChannelInfoDto;
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

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    // @Value 설정파일의 설정정보를 바인딩시킨다. (설정정보 의존성 주입)
    @Value("${channel.upload.save.directory}")
    private String saveDirectory;

    private final ChannelMapper channelMapper;
    private final PostMapper postMapper;

    public void createChannel(ChannelForm form) {
        String imageName = FileUtils.upload(form.getImageFile(), saveDirectory);
        User user = new User();
        user.setId(1);
        Channel channel = Channel.builder()
                .user(user)
                .title(form.getTitle())
                .description(form.getDescription())
                .imageName(imageName)
                .build();
        channelMapper.createChannel(channel);
    }

    public List<ChannelInfoDto> getAllChannels() {
        return channelMapper.getAllChannels();
    }

    public Channel getChannelByChannelId(int channelId) {
        return channelMapper.getChannelByChannelId(channelId);
    }

    public List<Channel> getChannelsByUserId(int userId) {
        return channelMapper.getChannelsByUserId(userId);
    }

    public void deleteChannel(Channel channel) {
        // 1. 채널을 삭제한다.
        channel.setDeleteYn("Y");
        channel.setDeleteDate(new Date());
        channelMapper.modifyChannel(channel);

        // 2. 채널과 관련된 글이 숨겨진다. (block)
        List<ChannelPost1> postList = postMapper.getAllPosts(channel.getId());
        for (ChannelPost1 post : postList) {
            post.setStatus("block");
            postMapper.modifyPost(post);
        }
    }

    public void createChannelParticipant(int channelId) {
        ChannelParticipant participant = new ChannelParticipant();
        participant.toParticipant(channelId, 1);
        channelMapper.createChannelParticipant(participant);
    }

    public void deleteChannelParticipant(int channelId) {
        ChannelParticipant participant = new ChannelParticipant();
        participant.toParticipant(channelId, 1);
        channelMapper.deleteChannelParticipant(participant);
    }

    public void modifyChannel(ChannelForm channelForm, Channel channel) {
        String imageName = FileUtils.upload(channelForm.getImageFile(), saveDirectory);
        channel.setTitle(channelForm.getTitle());
        channel.setDescription(channelForm.getDescription());
        channel.setImageName(imageName);
        channel.setUpdateDate(new Date());
        channelMapper.modifyChannel(channel);
    }
}
