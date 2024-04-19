package com.roommake.channel.mapper;

import com.roommake.channel.vo.Channelpost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    List<Channelpost> selectAllPosts(int channelId);
}
