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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaMapper qnaMapper;
    private final UserMapper userMapper;

    public Qna updateAnswer(int id, String answer, String email) {

        Qna qna = qnaMapper.getQnaById(id);
        User user = userMapper.getUserByEmail(email);

        qna.setAnswer(answer);
        qna.setAnswerWriter(user);
        qna.setAnswerYn("Y");
        qna.setUpdateDate(new Date());

        qnaMapper.updateAnswer(qna);

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
     * @param criteria getQnas에 전달해줄 기준 객체(답변여부 N 설정)
     * @return 반환된 미응답리스트(ListDto라서 .items()로 사용 가능)
     */
    public ListDto<Qna> getNoAnswerQnas(Criteria criteria) {
        criteria.setSort("N");
        criteria.setRows(qnaMapper.getTotalRows(criteria));
        return getQnas(criteria);
    }

    public Qna getQnaById(int id) {

        return qnaMapper.getQnaById(id);
    }

    public List<QnaCategory> getQnaCategories() {

        return qnaMapper.getQnaCategories();
    }

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
