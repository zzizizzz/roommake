package com.roommake.channel.mapper;

import com.roommake.channel.vo.ChannelPost;
import com.roommake.channel.vo.ChannelPostComplaint;
import com.roommake.channel.vo.ChannelPostLike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    List<ChannelPost> getAllPosts(int channelId);

    void createPost(ChannelPost post);

    void modifyPost(ChannelPost post);

    ChannelPost getPostByPostId(int postId);

    void addPostLike(ChannelPostLike postLike);

    ChannelPostLike getPostLikeUser(ChannelPostLike postLikeUser);

    void deletePostLike(ChannelPostLike postLike);

    void addPostComplaint(ChannelPostComplaint postComplaint);
}
