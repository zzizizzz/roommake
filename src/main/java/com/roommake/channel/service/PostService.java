package com.roommake.channel.service;

import com.roommake.channel.dto.ChannelDto;
import com.roommake.channel.dto.ChannelInfoDto;
import com.roommake.channel.dto.PostDto;
import com.roommake.channel.dto.PostForm;
import com.roommake.channel.enums.PostStatusEnum;
import com.roommake.channel.mapper.ChannelMapper;
import com.roommake.channel.mapper.PostMapper;
import com.roommake.channel.vo.*;
import com.roommake.community.mapper.CommunityMapper;
import com.roommake.community.vo.ComplaintCategory;
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
    private final CommunityMapper communityMapper;

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

    /**
     * 채널에 글을 등록한다.
     *
     * @param postForm  채널 글 등록폼
     * @param channelId 채널 아이디
     * @param userId    유저 아이디
     * @param image     "default.jpg" 또는 채널 등록시 업로드한 이미지의 S3Url
     */
    public void createPost(PostForm postForm, int channelId, int userId, String image) {
        Channel channel = Channel.builder().id(channelId).build();
        User user = User.builder().id(userId).build();
        ChannelPost post = ChannelPost.builder()
                .channel(channel)
                .user(user)
                .title(postForm.getTitle())
                .content(postForm.getContent())
                .imageName(image)
                .build();
        postMapper.createPost(post);
    }

    /**
     * 채널 글 상세를 조회한다.
     *
     * @param postId 채널 글 아이디
     * @param email  로그인한 유저 이메일
     * @return 채널 글 상세 (채널 글, 로그인한 유저의 좋아요 여부)
     */
    public PostDto getPostDetail(int postId, String email) {
        PostDto postDto = new PostDto();
        ChannelPost post = postMapper.getPostByPostId(postId);
        List<ComplaintCategory> complaintCategories = communityMapper.getComplaintCategories();
        postDto.setPost(post);
        postDto.setComplaintCategories(complaintCategories);

        if (email != null) {
            User user = userMapper.getUserByEmail(email);
            ChannelPostLike postLikeUser = ChannelPostLike.builder().postId(postId).userId(user.getId()).build();
            if (postMapper.getPostLikeUser(postLikeUser) != null) {
                postDto.setLike(true);
            }
        }
        return postDto;
    }

    /**
     * 채널 글을 조회한다.
     *
     * @param postId 채널 글 아이디
     * @return 채널 글
     */
    public ChannelPost getPostByPostId(int postId) {
        return postMapper.getPostByPostId(postId);
    }

    /**
     * 채널 글을 수정한다.
     *
     * @param postForm 채널 글 수정폼
     * @param image    "default.jpg" 또는 채널 등록시 업로드한 이미지의 S3Url
     * @param post     채널 글
     */
    public void modifyPost(PostForm postForm, String image, ChannelPost post) {
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setUpdateDate(new Date());
        post.setImageName(image);
        postMapper.modifyPost(post);
    }

    /**
     * 채널 글을 삭제한다.
     *
     * @param post 채널 글
     */
    public void deletePost(ChannelPost post) {
        post.setDeleteDate(new Date());
        post.setDeleteYn("Y");
        post.setStatus(PostStatusEnum.DELETE.getStatus());
        postMapper.modifyPost(post);
    }

    /**
     * 채널 글의 좋아요를 추가한다.
     *
     * @param userId 유저 아이디
     * @param postId 채널 글 아이디
     * @return 채널 글 좋아요수
     */
    public int addPostLike(int postId, int userId) {
        ChannelPostLike postLike = ChannelPostLike.builder().postId(postId).userId(userId).build();
        postMapper.addPostLike(postLike);
        ChannelPost post = postMapper.getPostByPostId(postId);
        int postLikeCount = post.getLikeCount() + 1;
        post.setLikeCount(postLikeCount);
        postMapper.modifyPost(post);
        return postLikeCount;
    }

    /**
     * 채널 글의 좋아요를 삭제(취소)한다.
     *
     * @param postId 채널 글 아이디
     * @param userId 유저 아이디
     * @return 채널 글 좋아요 수
     */
    public int deletePostLike(int postId, int userId) {
        ChannelPostLike postLike = ChannelPostLike.builder().postId(postId).userId(userId).build();
        postMapper.deletePostLike(postLike);
        ChannelPost post = postMapper.getPostByPostId(postId);
        int postLikeCount = post.getLikeCount() - 1;
        post.setLikeCount(postLikeCount);
        postMapper.modifyPost(post);
        return postLikeCount;
    }

    /**
     * 채널 글을 신고한다.
     *
     * @param postId         채널 글 아이디
     * @param complaintCatId 신고 카테고리 번호
     * @param userId         유저 아이디
     */
    public void addPostComplaint(int postId, int complaintCatId, int userId) {
        ChannelPost post = ChannelPost.builder().id(postId).build();
        ComplaintCategory complaintCat = ComplaintCategory.builder().id(complaintCatId).build();
        User user = User.builder().id(userId).build();
        ChannelPostComplaint postComplaint = ChannelPostComplaint.builder()
                .post(post)
                .user(user)
                .complaintCat(complaintCat)
                .build();
        postMapper.addPostComplaint(postComplaint);
    }
}
