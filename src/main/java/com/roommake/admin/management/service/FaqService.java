package com.roommake.admin.management.service;

import com.roommake.admin.management.dto.FaqForm;
import com.roommake.admin.management.mapper.FaqMapper;
import com.roommake.admin.management.vo.Faq;
import com.roommake.admin.management.vo.FaqCategory;
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
public class FaqService {

    private final UserMapper userMapper;
    private final FaqMapper faqMapper;

    public void createFaq(FaqForm form, String email) {

        User user = userMapper.getUserByEmail(email);
        FaqCategory faqCategory = FaqCategory.builder().id(form.getCategory()).build();

        Faq faq = Faq
                .builder()
                .title(form.getTitle())
                .content(form.getContent())
                .createDate(new Date())
                .updateDate(new Date())
                .category(faqCategory)
                .createByUser(user)
                .updateByUser(user)
                .build();

        faqMapper.createFaq(faq);
    }

    public List<FaqCategory> getFaqCategories() {
        return faqMapper.getFaqCategories();
    }

    public Faq modifyFaq(int id, FaqForm form, String email) {

        Faq faq = faqMapper.getFaqById(id);
        User user = userMapper.getUserByEmail(email);

        faq.setTitle(form.getTitle());
        faq.setContent(form.getContent());
        faq.getFaqCategory(form.getCategory());
        faq.setUpdateByUser(user);
        faq.setUpdateDate(new Date());
        return faq;
    }

    public void deleteFaq(int id) {
        Faq faq = faqMapper.getFaqById(id);
        faq.setDeleteYn("Y");
        faq.setDeleteDate(new Date());
        faqMapper.modifyFaq(faq);
    }

    public ListDto<Faq> getFaqs(Criteria criteria) {

        int totalRows = faqMapper.getTotalRows(criteria);

        Pagination pagination = new Pagination(criteria.getPage(), totalRows, criteria.getRows());

        criteria.setBegin(pagination.getBegin());
        criteria.setEnd(pagination.getEnd());

        List<Faq> faqs = faqMapper.getFaqs(criteria);

        ListDto<Faq> dto = new ListDto<>(faqs, pagination);
        return dto;
    }

    public Faq getFaqById(int id) {
        return faqMapper.getFaqById(id);
    }
}
