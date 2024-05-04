package com.roommake.user.mapper;

import com.roommake.dto.Criteria;
import com.roommake.user.vo.PlusPointHistory;
import com.roommake.user.vo.Term;
import com.roommake.user.vo.TermAgreement;
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

    // 아이디로 유저 조회
    User getUserById(int userId);

    // 추천인 코드 중복 여부 확인
    boolean existRecommendCode(String uniqueRecommendCode);

    // 모든 약관 조회
    List<Term> getAllTerms();

    // 유저 약관 동의 저장
    void createTermAgreement(TermAgreement termAgreement);

    // 약관 ID로 약관 정보를 조회
    Term getTermById(int id);

    // 추천인 코드로 유저 검색
    User getByRecommendCode(String recommendCode);

    // 신규 가입자에게 포인트 적립
    void addPlusPointForNewUser(PlusPointHistory plusPointHistory);

    // 기존 회원에게 포인트 적립
    void addPlusPointForExistUser(PlusPointHistory plusPointHistory);

    // 기존 회원 포인트 적립내역 업데이트
    void modifyUserPoint(int id, int point);

    // 신규 회원 포인트 적립내역 업데이트
    void modifyNewUserPoint(int id, int point);

    // 이메일 중복 여부 확인
    boolean ExistEmail(String email);

    // 유저 회원정보 수정
    void modifyUser(User user);

    // 조건에 맞는 유저 수 조회
    int getTotalRows(Criteria criteria);

    // 기준에 맞는 유저 목록 조회(페이징, 정렬, 검색)
    List<User> getUsers(Criteria criteria);

    // 유저 신고 카운트 누적
    void modifyUserComplaintCount(User user);
}
