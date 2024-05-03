package com.roommake.community.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.roommake.community.dto.CommCriteria;
import com.roommake.community.dto.CommunityForm;
import com.roommake.community.dto.MyPageCommunity;
import com.roommake.community.enums.CommStatusEnum;
import com.roommake.community.mapper.CommunityMapper;
import com.roommake.community.vo.Community;
import com.roommake.community.vo.CommunityCategory;
import com.roommake.config.S3Config;
import com.roommake.dto.ListDto;
import com.roommake.dto.Pagination;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private String localLocation = "C:/roommake/src/main/resources/static/images/";
    private final S3Config s3Config;

    private final CommunityMapper communityMapper;

    /**
     * CKEditor에 업로드한 이미지를 s3에 저장한다.
     *
     * @param request
     * @return 이미지 s3Url
     * @throws IOException
     */
    public String imageUpload(MultipartRequest request) throws IOException {

        // request 이미지 파일을 얻는다.
        MultipartFile file = request.getFile("upload");

        // 이미지 파일에서 이름 및 확장자를 얻는다.
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        // 이미지 파일 이름 유일성을 위해 uuid 생성
        String uuidFileName = UUID.randomUUID() + ext;

        // 서버환경에 저장할 경로 생성
        String localPath = localLocation + uuidFileName;

        // 서버환경에 이미지 파일을 저장
        File localFile = new File(localPath);
        file.transferTo(localFile);

        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        // 서버에 저장한 이미지를 삭제
        localFile.delete();

        return s3Url;
    }

    /**
     * 커뮤니티 글을 모두 조회한다.
     *
     * @param commCatId 커뮤니티 카테고리 아이디
     * @param criteria  정렬 및 페이징 정보
     * @return 커뮤니티 글 목록, 정렬 및 페이징
     */
    public ListDto<Community> getAllCommunitiesByCatId(int commCatId, CommCriteria criteria) {
        criteria.setCommCatId(commCatId);
        int totalRows = communityMapper.getCommTotalRowByCommId(criteria);

        Pagination pagination = new Pagination(criteria.getPage(), totalRows, criteria.getRows());
        criteria.setBegin(pagination.getBegin());
        criteria.setEnd(pagination.getEnd());

        List<Community> houseCommList = communityMapper.getAllCommunitiesByCatId(criteria);
        return new ListDto<Community>(houseCommList, pagination);
    }

    /**
     * 커뮤니티 카테고리를 모두 조회한다.
     *
     * @return 커뮤니티 카테고리 목록
     */
    public List<CommunityCategory> getAllCommCategories() {
        return communityMapper.getAllCommCategories();
    }

    /**
     * 커뮤니티 글을 등록한다.
     *
     * @param communityForm 커뮤니티 글 등록폼
     * @param s3Url         이미지 s3Url
     * @param userId        커뮤니티 글을 작성한 유저 아이디
     */
    public void createCommunity(CommunityForm communityForm, String s3Url, int userId) {
        User user = User.builder().id(userId).build();
        Community community = Community.builder()
                .category(new CommunityCategory(communityForm.getCategoryId()))
                .user(user)
                .title(communityForm.getTitle())
                .content(communityForm.getContent())
                .imageName(s3Url)
                .build();
        communityMapper.createCommunity(community);
    }

    /**
     * 커뮤니티 글 상세정보를 조회한다.
     *
     * @param commId 커뮤니티 글 아이디
     * @return 커뮤니티 글 상세정보
     */
    public Community getCommunityByCommId(int commId) {
        return communityMapper.getCommunityByCommId(commId);
    }

    public void modifyCommunity(CommunityForm communityForm, String image, Community community) {
        community.setTitle(communityForm.getTitle());
        community.setContent(communityForm.getContent());
        community.setUpdateDate(new Date());
        community.setImageName(image);
        communityMapper.modifyPost(community);
    }

    public void deleteCommunity(Community community) {
        community.setDeleteDate(new Date());
        community.setStatus(CommStatusEnum.DELETE.getStatus());
        community.setDeleteYn("Y");
        communityMapper.modifyPost(community);
    }

    // 사용자 ID로 게시글 정보 조회
    public List<MyPageCommunity> getCommunitiesByUserId(int userId) {
        return communityMapper.getCommunitiesByUserId(userId);
    }

    // 사용자 ID로 사용자가 작성한 총 게시물 수 조회
    public int countCommunitiesByUserId(int userId) {
        return communityMapper.countCommunitiesByUserId(userId);
    }

    // 사용자 ID로 사용자가 작성한 게시글의 총 댓글 수 조회
    public int countRepliesByUserId(int userId) {
        return communityMapper.countRepliesByUserId(userId);
    }
}
