package com.roommake.user.service;

import com.roommake.user.dto.UserSignupForm;
import com.roommake.user.exception.AlreadyUsedEmailException;
import com.roommake.user.exception.EmailException;
import com.roommake.user.mapper.UserMapper;
import com.roommake.user.mapper.UserRoleMapper;
import com.roommake.user.vo.User;
import com.roommake.user.vo.UserRole;
import com.roommake.utils.UniqueRecommendCodeUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;  // 비밀번호 암호화
    private final JavaMailSender mailSender;        // 이메일 발송
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    private Map<String, UserService.VerificationDetails> verifyCodes = new ConcurrentHashMap<>();

    // 닉네임 중복 확인
    public boolean isNicknameUnique(String nickname) {
        User foundUser = userMapper.getUserByNickname(nickname);
        return foundUser == null;
    }

    // 모든 유저 조회
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    // 이메일로 유저 조회
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    // 유저 등록
    @Transactional
    public User createUser(UserSignupForm form) {
        String email = form.getEmail1() + "@" + form.getEmail2();
        User foundUser = userMapper.getUserByEmail(email);
        if (foundUser != null) {
            throw new AlreadyUsedEmailException("[" + email + "]는 이미 사용중인 이메일입니다.");
        }

        // 랜덤 추천인 코드 생성 및 중복 체크
        String UniqueRecommendCode;
        do {
            UniqueRecommendCode = UniqueRecommendCodeUtils.createUniqueRecommendCode();
        } while (userMapper.existRecommendCode(UniqueRecommendCode)); // 중복된 코드가 있으면 다시 생성

        // User 객체 생성
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(form.getPassword()))
                .nickname(form.getNickname())
                .uniqueRecommendCode(UniqueRecommendCode)
                .optionRecommendCode(form.getOptionRecommendCode())
                .build();

        userMapper.createUser(user);

        // UserRole 객체 생성
        UserRole userRole = UserRole.builder()
                .user(user)
                .name("ROLE_USER")
                .build();

        userRoleMapper.createUserRole(userRole);

        return user;
    }

    /**
     * 주어진 이메일 주소로 인증 코드를 발송
     *
     * @param toEmail 인증 코드를 받을 이메일 주소
     * @throws EmailException 이메일 발송 실패시 예외를 발생
     */
    public void sendVerifyCode(String toEmail) throws EmailException {
        String code = generateVerificationCode();
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("hyunji9886@knou.ac.kr");
            helper.setTo(toEmail);
            helper.setSubject("[룸메이크] 인증코드 안내");

            String content = loadHtmlTemplate(code);
            helper.setText(content, true);

            mailSender.send(message);

            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(3);
            verifyCodes.put(toEmail, new UserService.VerificationDetails(code, expirationTime));
        } catch (MessagingException e) {
            throw new EmailException("이메일 서비스 중 오류 발생", e);
        }
    }

    /**
     * 이메일 주소와 인증 코드를 검증
     *
     * @param email 검증할 이메일 주소
     * @param code  검증할 인증 코드
     * @return boolean 코드가 유효하면 true,아니면 false 반환
     */
    public boolean verifyEmail(String email, String code) {
        UserService.VerificationDetails details = verifyCodes.get(email);
        return details != null && details.getCode().equals(code) &&
                LocalDateTime.now().isBefore(details.getExpirationTime());
    }

    /**
     * 6자리 랜덤 숫자 인증 코드를 생성
     *
     * @return String 생성된 인증 코드
     */
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * HTML 이메일 템플릿을 로드하고 인증 코드를 삽입
     *
     * @param code 삽입할 인증 코드
     * @return String 완성된 HTML 문자열
     */
    private String loadHtmlTemplate(String code) throws EmailException {
        ClassPathResource resource = new ClassPathResource("templates/user/verify-email.html");
        String htmlTemplate;
        try {
            htmlTemplate = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new EmailException("이메일 템플릿을 로드하는 도중 오류가 발생했습니다.", e);
        }
        return htmlTemplate.replace("${code}", code);
    }

    /**
     * 인증 코드와 만료 시간을 관리하기 위한 내부 클래스
     */
    private static class VerificationDetails {
        private String code;
        private LocalDateTime expirationTime;

        public VerificationDetails(String code, LocalDateTime expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
