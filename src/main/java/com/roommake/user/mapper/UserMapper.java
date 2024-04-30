package com.roommake.user.mapper;

import com.roommake.dto.Criteria;
import com.roommake.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    // 모든 유저 조회
    List<User> getAllUsers();

    // 이메일로 사용자 및 권한 조회
    Map<String, Object> getUserByEmailWithRoles(String email);

    // 이메일로 유저 조회
    User getUserByEmail(String email);

    // 유저 등록
    void createUser(User user);

    // 닉네임으로 유저 조회
    User getUserByNickname(String nickname);

    // 추천인 코드 중복 여부 확인
    boolean existRecommendCode(String uniqueRecommendCode);

    // 조건에 맞는 유저 수 조회
    int getTotalRows(Criteria criteria);

    // 조건에 맞는 유저 목록 조회
    List<User> getUsers(Criteria criteria);
}
