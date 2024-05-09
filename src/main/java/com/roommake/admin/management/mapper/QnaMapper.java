package com.roommake.admin.management.mapper;

import com.roommake.admin.management.vo.Qna;
import com.roommake.admin.management.vo.QnaCategory;
import com.roommake.dto.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QnaMapper {
    void createQna(Qna qna);

    void updateAnswer(Qna qna);

    Qna getQnaById(int id);

    int getTotalRows(Criteria criteria);

    List<Qna> getQnas(Criteria criteria);

    List<QnaCategory> getQnaCategories();

    QnaCategory getQnaCategory(int catId);

    List<Qna> getNoAnswerQnas();

    List<Qna> getNoAnswerQnasByUserId(int userId);

    List<Qna> getAnswerQnasByUserId(int userId);
}
