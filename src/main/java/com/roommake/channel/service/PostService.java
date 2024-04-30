package com.roommake.channel.service;

import com.roommake.channel.dto.*;
import com.roommake.channel.enums.PostStatusEnum;
import com.roommake.channel.mapper.ChannelMapper;
import com.roommake.channel.mapper.PostMapper;
import com.roommake.channel.mapper.PostReplyMapper;
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
    private final PostReplyMapper postReplyMapper;

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
     * @return 채널 글 상세 (채널 글, 로그인한 유저의 좋아요 여부, 총 댓글 갯수)
     */
    public PostDto getPostDetail(int postId, String email) {
        // 글 상세, 신고 카테고리 정보
        PostDto postDto = new PostDto();
        ChannelPost post = postMapper.getPostByPostId(postId);
        List<ComplaintCategory> complaintCategories = communityMapper.getComplaintCategories();
        postDto.setPost(post);
        postDto.setComplaintCategories(complaintCategories);

        // 글에 좋아요를 눌렀는지 여부
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
     * 해당 채널 글의 댓글을 모두 조회한다.
     *
     * @param postId 채널 글 아이디
     * @return 채널 글 전체 댓글
     */
    public PostReplyDto getAllPostReplies(int postId, String email) {
        int totalReplyCount = postReplyMapper.getTotalReplyCountByPostId(postId);
        List<?> postReplies;
        if (email != null) {
            User user = userMapper.getUserByEmail(email);
            postReplies = postReplyMapper.getAllRepliesByPostIdAndUserId(postId, user.getId());
        } else {
            postReplies = postReplyMapper.getAllRepliesByPostId(postId);
        }
        PostReplyDto replyDto = new PostReplyDto();
        replyDto.setTotalReplyCount(totalReplyCount);
        replyDto.setPostReplies(postReplies);

        return replyDto;
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
        ChannelPost post = postMapper.getPostByPostId(postId);
        ComplaintCategory complaintCat = ComplaintCategory.builder().id(complaintCatId).build();
        User user = User.builder().id(userId).build();
        ChannelPostComplaint postComplaint = ChannelPostComplaint.builder()
                .post(post)
                .user(user)
                .complaintCat(complaintCat)
                .build();
        postMapper.addPostComplaint(postComplaint);
    }

    /**
     * 채널 글에 댓글을 등록한다.
     *
     * @param postId  채널 글 아이디
     * @param content 댓글 내용
     * @param userId  유저 아이디
     */
    public void createPostReply(int postId, String content, int userId) {
        ChannelPost post = ChannelPost.builder().id(postId).build();
        User user = User.builder().id(userId).build();
        ChannelPostReply postReply = ChannelPostReply.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
        postReplyMapper.createPostReply(postReply);

        postReply.setGroupId(postReply.getId());
        postReplyMapper.modifyReplyGroupId(postReply);
    }

    /**
     * 채널 글에 대댓글을 등록한다.
     *
     * @param postId  채널 글 아이디
     * @param content 대댓글 내용
     * @param userId  유저 아이디
     */
    public void createPostReReply(int postId, String content, int parentsReplyId, int userId) {
        ChannelPost post = ChannelPost.builder().id(postId).build();
        User user = User.builder().id(userId).build();
        ChannelPostReply postReply = ChannelPostReply.builder()
                .post(post)
                .user(user)
                .content(content)
                .parentsId(parentsReplyId)
                .groupId(parentsReplyId)
                .build();
        postReplyMapper.createPostReReply(postReply);
    }

    /**
     * 채널 글의 댓글을 조회한다.
     *
     * @param replyId 댓글 아이디
     * @return 댓글
     */
    public ChannelPostReply getPostReplyByReplyId(int replyId) {
        return postReplyMapper.getReplyByReplyId(replyId);
    }

    /**
     * 채널 글의 댓글을 신고한다.
     *
     * @param replyId        채널 댓글 아이디
     * @param complaintCatId 신고 카테고리 번호
     * @param userId         유저 아이디
     */
    public void addPostReplyComplaint(int replyId, int complaintCatId, int userId) {
        ChannelPostReply postReply = postReplyMapper.getReplyByReplyId(replyId);
        ComplaintCategory complaintCat = ComplaintCategory.builder().id(complaintCatId).build();
        User user = User.builder().id(userId).build();
        ChannelPostReplyComplaint postReplyComplaint = ChannelPostReplyComplaint.builder()
                .reply(postReply)
                .user(user)
                .complaintCat(complaintCat)
                .build();
        postReplyMapper.addReplyComplaint(postReplyComplaint);
    }

    /**
     * 채널 글의 댓글을 수정한다.
     *
     * @param postReply 수정 전 댓글
     * @param content   수정한 댓글 내용
     * @return 수정 후 댓글
     */
    public ChannelPostReply modifyReply(ChannelPostReply postReply, String content) {
        postReply.setContent(content);
        postReply.setUpdateDate(new Date());
        postReplyMapper.modifyReply(postReply);
        return postReply;
    }

    /**
     * 댓글 아이디로 댓글을 삭제한다.
     *
     * @param postReply 삭제할 댓글
     */
    public void deletePostReplyByReplyId(ChannelPostReply postReply) {
        int reReplyCount = postReplyMapper.getReReplyCount(postReply.getId());
        if (reReplyCount == 0) {
            postReply.setStatus(PostStatusEnum.DELETE.getStatus());
        }
        postReply.setDeleteDate(new Date());
        postReply.setDeleteYn("Y");
        postReplyMapper.modifyReply(postReply);
    }
}
