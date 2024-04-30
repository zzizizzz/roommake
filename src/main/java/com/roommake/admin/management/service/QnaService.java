package com.roommake.admin.management.service;

import com.roommake.admin.management.mapper.QnaMapper;
import com.roommake.admin.management.vo.Qna;
import com.roommake.admin.management.vo.QnaCategory;
import com.roommake.dto.Criteria;
import com.roommake.dto.ListDto;
import com.roommake.dto.Pagination;
import com.roommake.user.mapper.UserMapper;
import com.roommake.user.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaMapper qnaMapper;
    private final UserMapper userMapper;
    private final MailService mailService;

    /**
     * 문의사항번호, 답변내용, 답변자를 받아서 문의사항 답변을 등록한다.
     *
     * @param id
     * @param answer
     * @param email
     * @return
     */
    public Qna updateAnswer(int id, String answer, String email) throws Exception {
        long beforeTime = System.currentTimeMillis();

        Qna qna = qnaMapper.getQnaById(id);
        User user = userMapper.getUserByEmail(email);

        qna.setAnswer(answer);
        qna.setAnswerWriter(user);
        qna.setAnswerYn("Y");
        qna.setUpdateDate(new Date());

        qnaMapper.updateAnswer(qna);

        String to = qna.getUser().getEmail();
        String subject = "문의사항 답변이 등록되었습니다.";
        String html = mailService.qnaHtmlTemplate(qna.getTitle()); // load
        mailService.sendEmail(to, subject, html);

        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        log.info("답변 등록 후 메일발송 총 실행 시간: " + diffTime + "ms");
        return qna;
    }

    public ListDto<Qna> getQnas(Criteria criteria) {

        int totalRows = qnaMapper.getTotalRows(criteria);

        Pagination pagination = new Pagination(criteria.getPage(), totalRows, criteria.getRows());

        criteria.setBegin(pagination.getBegin());
        criteria.setEnd(pagination.getEnd());

        List<Qna> qnas = qnaMapper.getQnas(criteria);

        ListDto<Qna> dto = new ListDto<>(qnas, pagination);

        return dto;
    }

    /**
     * 미응답 문의사항 리스트 조회
     *
     * @return 반환된 미응답리스트(ListDto라서 .items()로 사용 가능)
     */
    public List<Qna> getNoAnswerQnas() {
        return qnaMapper.getNoAnswerQnas();
    }

    /**
     * 문의 사항 1개 조회
     *
     * @param id 조회할 문의사항 번호
     * @return 조회된 문의사항
     */
    public Qna getQnaById(int id) {

        return qnaMapper.getQnaById(id);
    }

    /**
     * 문의사항 카테고리 리스트 조회
     *
     * @return 문의사항 카테고리 리스트
     */
    public List<QnaCategory> getQnaCategories() {

        return qnaMapper.getQnaCategories();
    }

    /**
     * 문의사항 카테고리 번호로 카테고리 정보 조회
     *
     * @param qnaCatId 조회할 카테고리 아이디
     * @return 문의사항 카테고리
     */
    public QnaCategory getQnaCategory(int qnaCatId) {

        return qnaMapper.getQnaCategory(qnaCatId);
    }



    /*
    답변등록 구현 후 이메일 추가하기
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendMail(QnaMailDto mailDto) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setSubject("문의사항에 대한 답변이 등록되었습니다.");
        helper.setTo(mailDto.getTo());
        helper.setFrom(fromEmail);

        HashMap<String, Object> emailValues = new HashMap<>();
        emailValues.put("qna", "qna");

        Context context = new Context();
        emailValues.forEach((key, value) -> {
            context.setVariable(key, value);
        });

        String html = templateEngine.process(mailDto.getTemplate(), context);
        helper.setText(html, true);

        javaMailSender.send(message);
    }*/
}
