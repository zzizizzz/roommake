package com.roommake.channel.mapper;

import com.roommake.channel.vo.ChannelPost1;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    List<ChannelPost1> getAllPosts(int channelId);

    void createPost(ChannelPost1 post);

    void modifyPost(ChannelPost1 post);
}
