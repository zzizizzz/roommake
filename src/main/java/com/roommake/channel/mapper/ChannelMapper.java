package com.roommake.channel.mapper;

import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.ChannelParticipant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChannelMapper {
    void createChannel(Channel channel);

    List<Channel> getAllChannels();

    Channel getChannelById(int channelId);

    void createChannelParticipant(ChannelParticipant participant);

    ChannelParticipant getChannelParticipant(ChannelParticipant participant);

    void deleteChannelParticipant(ChannelParticipant participant);
}
