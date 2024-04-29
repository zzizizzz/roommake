package com.roommake.channel.mapper;

import com.roommake.channel.vo.ChannelPostReply;
import com.roommake.channel.vo.ChannelPostReplyComplaint;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostReplyMapper {

    int getTotalReplyCountByPostId(int postId);

    List<ChannelPostReply> getAllRepliesByPostId(int postId);

    void createPostReply(ChannelPostReply postReply);

    void createPostReReply(ChannelPostReply postReply);

    void modifyReplyGroupId(ChannelPostReply postReply);

    ChannelPostReply getReplyByReplyId(int replyId);

    void addReplyComplaint(ChannelPostReplyComplaint postReplyComplaint);
}
