package com.roommake.channel.mapper;

import com.roommake.channel.dto.ChannelPostReplyDto;
import com.roommake.channel.vo.ChannelPostReply;
import com.roommake.channel.vo.ChannelPostReplyComplaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostReplyMapper {

    int getTotalReplyCountByPostId(int postId);

    List<ChannelPostReply> getAllRepliesByPostId(int postId);

    List<ChannelPostReplyDto> getAllRepliesByPostIdAndUserId(@Param("postId") int postId, @Param("userId") int userId);

    void createPostReply(ChannelPostReply postReply);

    void createPostReReply(ChannelPostReply postReply);

    void modifyReplyGroupId(ChannelPostReply postReply);

    ChannelPostReply getReplyByReplyId(int replyId);

    void addReplyComplaint(ChannelPostReplyComplaint postReplyComplaint);

    void modifyReply(ChannelPostReply postReply);

    int getReReplyCount(int replyId);
}
