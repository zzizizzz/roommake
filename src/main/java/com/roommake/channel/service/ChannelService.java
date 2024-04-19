package com.roommake.channel.service;

import com.roommake.channel.dto.ChannelCreateForm;
import com.roommake.channel.mapper.ChannelMapper;
import com.roommake.channel.vo.Channel;
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

    public void createChannel(ChannelCreateForm form) {

        String imageName = FileUtils.upload(form.getImageFile(), saveDirectory);
        User user = new User();
        user.setId(1);

        Channel channel = Channel.builder()
                .user(user)
                .title(form.getTitle())
                .description(form.getDescription())
                .imageName(imageName)
                .build();

        channelMapper.insertChannel(channel);
    }

    public List<Channel> getAllChannel() {
        return channelMapper.selectAllChannel();
    }

    public Channel getChannelById(int channelId) {
        return channelMapper.selectChannel(channelId);
    }
}
