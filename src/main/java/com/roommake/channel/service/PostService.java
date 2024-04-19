package com.roommake.channel.service;

import com.roommake.channel.mapper.PostMapper;
import com.roommake.channel.vo.Channelpost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    public List<Channelpost> getAllPosts(int channelId) {
        return postMapper.selectAllPosts(channelId);
    }
}
