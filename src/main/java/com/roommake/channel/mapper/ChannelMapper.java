package com.roommake.channel.mapper;

import com.roommake.channel.vo.Channel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChannelMapper {
    void insertChannel(Channel channel);
}
