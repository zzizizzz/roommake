package com.roommake.channel.service;

import com.roommake.channel.dto.ChannelForm;
import com.roommake.channel.dto.ChannelInfoDto;
import com.roommake.channel.enums.StatusEnum;
import com.roommake.channel.mapper.ChannelMapper;
import com.roommake.channel.mapper.PostMapper;
import com.roommake.channel.vo.Channel;
import com.roommake.channel.vo.ChannelParticipant;
import com.roommake.channel.vo.ChannelPost;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelMapper channelMapper;
    private final PostMapper postMapper;

    /**
     * 천체 채널 목록을 조회한다.
     *
     * @return 채널정보, 총 참여자수, 총 글개수가 포함된 전체 채널 목록
     */
    public List<ChannelInfoDto> getAllChannels() {
        return channelMapper.getAllChannels();
    }

    /**
     * 로그인한 유저가 활동중인 채널 목록을 조회한다.
     *
     * @param userId 로그인한 유저 아이디
     * @return 활동중인 채널 목록
     */
    public List<Channel> getChannelsByUserId(int userId) {
        return channelMapper.getChannelsByUserId(userId);
    }

    /**
     * 채널을 조회한다.
     *
     * @param channelId 채널 아이디
     * @return 채널 정보
     */
    public Channel getChannelByChannelId(int channelId) {
        return channelMapper.getChannelByChannelId(channelId);
    }

    /**
     * 채널을 추가한다.
     *
     * @param form      채널 등록폼
     * @param imageName "default.jpg" 또는 채널 등록시 업로드한 이미지의 S3Url
     * @param userId    채널을 등록한 유저 아이디
     */
    public void createChannel(ChannelForm form, String imageName, int userId) {
        User user = User.builder().id(userId).build();
        Channel channel = Channel.builder()
                .user(user)
                .title(form.getTitle())
                .description(form.getDescription())
                .imageName(imageName)
                .build();
        channelMapper.createChannel(channel);
    }

    /**
     * 채널 정보를 수정한다.
     *
     * @param channelForm 채널 수정폼
     * @param imageName   채널 수정시 업로드한 이미지의 3sUrl
     * @param channel     수정하려는 채널 정보
     */
    public void modifyChannel(ChannelForm channelForm, String imageName, Channel channel) {
        channel.setTitle(channelForm.getTitle());
        channel.setDescription(channelForm.getDescription());
        channel.setImageName(imageName);
        channel.setUpdateDate(new Date());
        channelMapper.modifyChannel(channel);
    }

    /**
     * 채널을 삭제한다.
     *
     * @param channel 삭제할 채널정보
     */
    public void deleteChannel(Channel channel) {
        // 1. 채널을 삭제한다.
        channel.setDeleteYn("Y");
        channel.setDeleteDate(new Date());
        channelMapper.modifyChannel(channel);

        // 2. 채널과 관련된 글이 숨겨진다. (block)
        List<ChannelPost> postList = postMapper.getAllPosts(channel.getId());
        for (ChannelPost post : postList) {
            post.setStatus(StatusEnum.BLOCK.getStatus());
            postMapper.modifyPost(post);
        }
    }

    /**
     * 채널 참여자를 조회한다.
     *
     * @param channelId 채널 아이디
     * @param userId    유저 아이디
     */
    public ChannelParticipant getChannelParticipant(int channelId, int userId) {
        ChannelParticipant participant = new ChannelParticipant();
        participant.toParticipant(channelId, userId);
        return channelMapper.getChannelParticipant(participant);
    }

    /**
     * 로그인한 유저가 채널에 참여한다.
     *
     * @param channelId 채널 아이디
     * @param userId    유저 아이디
     */
    public void createChannelParticipant(int channelId, int userId) {
        ChannelParticipant participant = new ChannelParticipant();
        participant.toParticipant(channelId, userId);
        channelMapper.createChannelParticipant(participant);
    }

    /**
     * 로그인한 유저가 채널 참여를 취소한다.
     *
     * @param channelId 채널 아이디
     * @param userId    유저 아이디
     */
    public void deleteChannelParticipant(int channelId, int userId) {
        ChannelParticipant participant = new ChannelParticipant();
        participant.toParticipant(channelId, userId);
        channelMapper.deleteChannelParticipant(participant);
    }
}
