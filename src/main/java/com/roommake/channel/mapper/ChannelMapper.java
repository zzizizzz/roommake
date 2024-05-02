package com.roommake.channel.mapper;

import com.roommake.channel.dto.ChannelInfoDto;
import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.ChannelParticipant;
import com.roommake.channel.vo.ChannelPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChannelMapper {

    List<ChannelInfoDto> getAllChannels();

    List<Channel> getChannelsByUserId(int userId);

    Channel getChannelByChannelId(int channelId);

    void createChannel(Channel channel);

    void modifyChannel(Channel channel);

    void createChannelParticipant(ChannelParticipant participant);

    void deleteChannelParticipant(ChannelParticipant participant);

    ChannelInfoDto getUserAndPostCountChannelId(int channelId);

    ChannelParticipant getChannelParticipant(ChannelParticipant participant);

    List<ChannelPost> getAllChannelPosts(int id);
}
