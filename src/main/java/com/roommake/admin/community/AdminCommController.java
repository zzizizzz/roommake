package com.roommake.admin.community;

import com.roommake.community.dto.CommCriteria;
import com.roommake.community.service.CommunityService;
import com.roommake.community.vo.Community;
import com.roommake.community.vo.CommunityCategory;
import com.roommake.dto.ListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/community")
@RequiredArgsConstructor
public class AdminCommController {
    private final CommunityService communityService;

    @GetMapping("/commList")
    public String commList(@RequestParam(name = "commCatId", required = false, defaultValue = "1") int commCatId,
                           @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
                           @RequestParam(name = "sort", required = false, defaultValue = "all") String sort,
                           @RequestParam(name = "opt", required = false) String opt,
                           @RequestParam(name = "keyword", required = false) String keyword,
                           Model model) {

        CommCriteria criteria = new CommCriteria();

        criteria.setPage(page);
        criteria.setRows(rows);
        criteria.setSort(sort);

        if (StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
            criteria.setOpt(opt);
            criteria.setKeyword(keyword);
        }

        // community 카테고리 리스트
        List<CommunityCategory> communityCategories = communityService.getAllCommCategories();

        // 커뮤니티 카테고리id별 게시글 목록
        ListDto<Community> dto = communityService.getAllCommunitiesByCatId(commCatId, criteria);

        model.addAttribute("communityList", dto.getItems());
        model.addAttribute("paging", dto.getPaging());
        model.addAttribute("criteria", criteria);
        model.addAttribute("commCatId", commCatId);
        model.addAttribute("commCategories", communityCategories);

        return "admin/community/community";
    }

    @GetMapping("/channel")
    public String channelList() {
        return "admin/community/channel";
    }
}
