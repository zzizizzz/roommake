package com.roommake.channel.service;

import com.roommake.channel.dto.ChannelDto;
import com.roommake.channel.dto.ChannelForm;
import com.roommake.channel.dto.ChannelInfoDto;
import com.roommake.channel.mapper.ChannelMapper;
import com.roommake.channel.mapper.PostMapper;
import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.ChannelParticipant;
import com.roommake.channel.vo.Channelpost;
import com.roommake.user.vo.User;
import com.roommake.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public List<Channel> getChannelsByUserId(int userId) {
        return channelMapper.getChannelsByUserId(userId);
    }

    public ChannelDto getChannelById(int channelId, String email) {

        ChannelDto dto = new ChannelDto();

        Channel channel = channelMapper.getChannelByChannelId(channelId);
        List<Channelpost> channelPosts = postMapper.getAllPosts(channelId);

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
}
