package com.roommake.channel.mapper;

import com.roommake.channel.vo.ChannelPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    List<ChannelPost> getAllPosts(int channelId);

    void createPost(ChannelPost post);

    void modifyPost(ChannelPost post);

    ChannelPost getPostByPostId(int postId);
}
