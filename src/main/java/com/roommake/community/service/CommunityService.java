package com.roommake.community.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.roommake.community.dto.CommunityForm;
import com.roommake.community.mapper.CommunityMapper;
import com.roommake.community.vo.Community;
import com.roommake.community.vo.CommunityCategory;
import com.roommake.config.S3Config;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private String localLocation = "C:/roommake/src/main/resources/static/images/";
    private final S3Config s3Config;

    @Value("${community.upload.save.directory}")
    private String saveDirectory;

    private final CommunityMapper communityMapper;

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

    public List<CommunityCategory> getAllCommCategories() {
        return communityMapper.getAllCommCategories();
    }

    public void createCommunity(CommunityForm communityForm, String s3Url) {
        User user = new User();
        user.setId(6);

        Community community = Community.builder()
                .category(new CommunityCategory(communityForm.getCategoryId()))
                .user(user)
                .title(communityForm.getTitle())
                .content(communityForm.getContent())
                .imageName(s3Url)
                .build();
        communityMapper.createCommunity(community);
    }

    public List<Community> getAllCommunitiesByCatId(int commCatId) {
        return communityMapper.getAllCommunitiesByCatId(commCatId);
    }

    public Community getCommunityByCommId(int commId) {
        return communityMapper.getCommunityByCommId(commId);
    }
}
