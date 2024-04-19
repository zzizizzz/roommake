package com.roommake.channel.mapper;

import com.roommake.channel.vo.Channel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChannelMapper {
    void insertChannel(Channel channel);

    List<Channel> selectAllChannel();

    Channel selectChannel(int channelId);
}
