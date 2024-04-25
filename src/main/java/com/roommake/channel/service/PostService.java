package com.roommake.channel.service;

import com.roommake.channel.dto.ChannelDto;
import com.roommake.channel.dto.ChannelInfoDto;
import com.roommake.channel.dto.PostForm;
import com.roommake.channel.dto.Status;
import com.roommake.channel.mapper.ChannelMapper;
import com.roommake.channel.mapper.PostMapper;
import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.ChannelParticipant;
import com.roommake.channel.vo.ChannelPost;
import com.roommake.user.mapper.UserMapper;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final ChannelMapper channelMapper;
    private final UserMapper userMapper;

    /**
     * 채널 아이디로 채널 정보와 채널의 전체 글 목록을 조회한다.
     *
     * @param channelId 채널 아이디
     * @param email     로그인한 유저 이메일
     * @return 채널 정보, 참여자 수, 채널글 개수, 채널글 목록
     */
    public ChannelDto getAllPostsByChannelId(int channelId, String email) {

        ChannelDto dto = new ChannelDto();

        Channel channel = channelMapper.getChannelByChannelId(channelId);
        ChannelInfoDto chInfo = channelMapper.getUserAndPostCountChannelId(channelId);
        List<ChannelPost> channelPosts = postMapper.getAllPosts(channelId);

        dto.setChannel(channel);
        dto.setChannelParticipantCount(chInfo.getChannelParticipantCount());
        dto.setChannelPostCount(chInfo.getChannelPostCount());
        dto.setChannelPosts(channelPosts);

        if (email != null) {
            User user = userMapper.getUserByEmail(email);
            ChannelParticipant channelParticipant = new ChannelParticipant();
            channelParticipant.setChannel(channel);
            channelParticipant.setUser(user);
            if (channelMapper.getChannelParticipant(channelParticipant) != null) {
                dto.setParticipant(true);
            }
        }
        return dto;
    }

    public void createPost(int channelId, PostForm postForm, String s3Url) {
        User user = new User();
        user.setId(6);
        ChannelPost post = ChannelPost.builder()
                .channel(new Channel(channelId))
                .user(user)
                .title(postForm.getTitle())
                .content(postForm.getContent())
                .imageName(s3Url)
                .build();
        postMapper.createPost(post);
    }

    public ChannelPost getPostByPostId(int postId) {
        return postMapper.getPostByPostId(postId);
    }

    public void modifyPost(PostForm postForm, String image, ChannelPost post) {
        User user = new User();
        user.setId(6);
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setUpdateDate(new Date());
        post.setImageName(image);
        postMapper.modifyPost(post);
    }

    public void deletePost(ChannelPost post) {
        post.setDeleteDate(new Date());
        post.setDeleteYn("Y");
        post.setStatus(Status.DELETE.getStatus());
        postMapper.modifyPost(post);
    }
}
