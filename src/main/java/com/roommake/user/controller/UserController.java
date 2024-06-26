package com.roommake.user.controller;

import com.roommake.admin.management.service.QnaService;
import com.roommake.admin.management.vo.Qna;
import com.roommake.community.dto.MyPageCommunity;
import com.roommake.community.service.CommunityService;
import com.roommake.community.vo.CommunityCategory;
import com.roommake.dto.Message;
import com.roommake.dto.Pagination;
import com.roommake.product.service.ProductService;
import com.roommake.product.vo.ProductCategory;
import com.roommake.resolver.Login;
import com.roommake.user.dto.*;
import com.roommake.user.exception.AlreadyUsedEmailException;
import com.roommake.user.exception.AlreadyUsedNicknameException;
import com.roommake.user.mapper.UserMapper;
import com.roommake.user.security.LoginUser;
import com.roommake.user.service.UserService;
import com.roommake.user.vo.Term;
import com.roommake.user.vo.TermAgreement;
import com.roommake.user.vo.User;
import com.roommake.utils.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User API", description = "로그인, 회원가입, 마이페이지 API를 제공한다.")
public class UserController {

    private final UserService userService;
    private final CommunityService communityService;
    private final S3Uploader s3Uploader;
    private final ProductService productService;
    private final QnaService qnaService;
    private final UserMapper userMapper;

    @Operation(summary = "로그인 폼", description = "로그인 폼을 조회한다.")
    @GetMapping("/login")
    public String loginform(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("fail".equals(error)) {
            model.addAttribute("loginError", "이메일 또는 비밀번호를 확인해 주세요.");
        }
        return "/user/loginform"; // 로그인 페이지의 뷰 이름
    }

    @Operation(summary = "회원가입 폼", description = "회원가입 폼을 조회한다.")
    @GetMapping("/signup")
    public String sinupform(Model model) {
        List<Term> terms = userService.getAllTerms();
        model.addAttribute("userSignupForm", new UserSignupForm());
        model.addAttribute("terms", terms);
        return "/user/signupform";
    }

    @Operation(summary = "회원가입 등록", description = "신규 회원을 등록한다.")
    @PostMapping("/signup")
    @Transactional
    public String signup(@Valid UserSignupForm form, BindingResult errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return "/user/signupform";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "/user/signupform";
        }

        if (!userService.isNicknameUnique(form.getNickname())) {
            errors.rejectValue("nickname", "error.nickname", "이미 사용중인 닉네임입니다.");
            return "/user/signupform";
        }

        // 추천인 코드 입력 검증
        if (form.getOptionRecommendCode() != null && !form.getOptionRecommendCode().isEmpty()) {
            if (!userService.checkRecommendCodeExists(form.getOptionRecommendCode())) {
                errors.rejectValue("optionRecommendCode", "error.optionRecommendCode", "존재하지 않는 추천인 코드입니다.");
                return "/user/signupform";
            }
        }

        TermAgreement termAgreement = new TermAgreement();
        termAgreement.setAgree1(form.getTermAgreements1());
        termAgreement.setAgree2(form.getTermAgreements2());
        termAgreement.setAgree3(form.getTermAgreements3());

        if (form.getTermAgreements1() == null || form.getTermAgreements2() == null) {
            errors.reject("termAgreement", "모든 필수 약관에 동의해야 합니다.");
            return "/user/signupform";
        }

        try {
            User user = userService.createUser(form);
            // 약관 동의 정보 저장
            termAgreement.setUser(user);
            userService.agreeToTerms(termAgreement);

            // 기본 폴더 생성
            userService.createDefaultFolder(user.getId());

            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/user/login";
        } catch (AlreadyUsedEmailException ex) {
            errors.rejectValue("email", null, ex.getMessage());
            return "/user/signupform";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "/user/default-error";
        }
    }

    @Operation(summary = "닉네임 중복확인", description = "닉네임 중복을 확인하는 엔드포인트")
    @ResponseBody
    @PostMapping("/check-duplicate-nickname")
    public ResponseEntity<?> checkDuplicateNickname(@RequestParam String nickname) {
        try {
            boolean isDuplicate = userService.isNicknameUnique(nickname);
            return ResponseEntity.ok(isDuplicate);
        } catch (AlreadyUsedNicknameException e) {
            // 이미 사용된 닉네임인 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 닉네임입니다.");
        } catch (Exception e) {
            // 그 외 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("닉네임 중복 검사 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "약관 조회", description = "약관을 조회한다.")
    @GetMapping("/terms/{id}")
    public String viewTerm(@PathVariable int id, Model model) {
        Term term = userService.getTermById(id);
        List<String> contentLines = Arrays.asList(term.getContent().split("\\r?\\n"));
        boolean required = term.getRequireYn().equals("Y");
        model.addAttribute("term", term);
        model.addAttribute("contentLines", contentLines);
        model.addAttribute("required", required);

        switch (id) {
            case 1:
                return "user/term1";
            case 2:
                return "user/term2";
            case 3:
                return "user/term3";
            default:
                return "user/default-error";
        }
    }

    /**
     * 비밀번호 변경 - 이메일 인증
     *
     * @return
     */
    @GetMapping("/resetpwd1")
    public String resetpwd1() {
        return "user/reset-password";
    }

    /**
     * 비밀번호 변경 폼
     *
     * @return
     */
    @GetMapping("/resetpwd2")
    public String resetpwd2() {
        return "user/reset-passwordform";
    }

    @Operation(summary = "마이페이지 메인", description = "마이페이지 메인을 조회한다.")
    @GetMapping("/mypage")
    @PreAuthorize("isAuthenticated()")
    public String myPage(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                         Principal principal, Model model) {

        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        int userId = user.getId();
        int totalRows = communityService.getTotalRows(userId);

        // 사용자가 등록한 게시물 목록을 조회
        List<MyPageCommunity> communities = communityService.getCommunitiesByUserId(userId, page);
        model.addAttribute("communities", communities);

        // 사용자가 작성한 커뮤니티 게시글의 총 개수를 조회
        int communityCount = communityService.countCommunitiesByUserId(userId);
        model.addAttribute("communityCount", communityCount);

        // 모든 스크랩 조회
        List<AllScrap> allScraps = userService.getAllScraps(userId, 1);
        model.addAttribute("allScraps", allScraps);

        Pagination pagination = new Pagination(page, totalRows, 5);
        model.addAttribute("paging", pagination);

        int totalScrapCount = userService.getTotalScrapCount(userId);
        model.addAttribute("totalScrapCount", totalScrapCount);

        int totalLikes = userService.getTotalLikes(userId);
        model.addAttribute("totalLikes", totalLikes);

        // 마이페이지의 뷰 반환
        return "user/mypage-main";
    }

    /**
     * 마이페이지 - 커뮤니티
     *
     * @return
     */
    @Operation(summary = "마이페이지 커뮤니티", description = "마이페이지 커뮤니티 조회한다.")
    @GetMapping("/mycomm")
    @PreAuthorize("isAuthenticated()")
    public String mypage2(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                          Principal principal, Model model) {

        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        int userId = user.getId();

        int totalRows = communityService.getTotalRows(userId);

        Pagination pagination = new Pagination(page, totalRows, 5);
        model.addAttribute("paging", pagination);
        // 사용자가 등록한 게시물 목록을 조회
        List<MyPageCommunity> communities = communityService.getCommunitiesByUserId(userId, page);
        model.addAttribute("communities", communities);

        // 사용자가 작성한 커뮤니티 게시글의 총 개수를 조회
        int communityCount = communityService.countCommunitiesByUserId(userId);
        model.addAttribute("communityCount", communityCount);

        int totalScrapCount = userService.getTotalScrapCount(userId);
        model.addAttribute("totalScrapCount", totalScrapCount);

        int totalLikes = userService.getTotalLikes(userId);
        model.addAttribute("totalLikes", totalLikes);

        return "user/mypage-community";
    }

    /**
     * 마이페이지-스크랩북(모두)
     *
     * @return
     */
    @GetMapping("/scrapbook")
    public String scrapbook(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                            Principal principal, Model model) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        int userId = user.getId();

        int totalRows = communityService.getTotalRows(user.getId());
        // 모든 스크랩 조회
        List<AllScrap> allScraps = userService.getAllScraps(userId, page);
        // 화면에 표시할 페이징 정보
        Pagination pagination = new Pagination(page, totalRows, 30);

        // 타입별로 필터링된 스크랩 목록을 모델에 추가
        List<AllScrap> productScraps = allScraps.stream()
                .filter(scrap -> "Product".equals(scrap.getType()))
                .collect(Collectors.toList());
        List<AllScrap> communityScraps = allScraps.stream()
                .filter(scrap -> "Community".equals(scrap.getType()))
                .collect(Collectors.toList());

        model.addAttribute("productScraps", productScraps);
        model.addAttribute("communityScraps", communityScraps);
        model.addAttribute("allScraps", allScraps);
        model.addAttribute("paging", pagination);

        // 모든 폴더 조회
        List<AllScrap> recentScraps = userService.getScrapFolders(userId);
        model.addAttribute("recentScraps", recentScraps);

        // 각 카테고리별 개수 계산
        int productCount = productScraps.size();
        int communityCount = communityScraps.size();
        int totalScrapCount = allScraps.size();

        model.addAttribute("productCount", productCount);
        model.addAttribute("communityCount", communityCount);
        model.addAttribute("totalScrapCount", totalScrapCount);

        return "user/mypage-scrapbook";
    }

    @Operation(summary = "스크랩 폴더", description = "마이페이지 스크랩 폴더 카테고리를 조회한다.")
    @GetMapping("/scrapbook1")
    public String scrapbookWithAllScraps(Principal principal, Model model) {
        // 사용자 이메일 확인 및 조회
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        // 사용자 ID로 필요한 스크랩과 폴더 정보 가져오기
        int userId = user.getId();

        // 모든 스크랩 조회
        List<AllScrap> allScraps = userService.getAllScraps(userId, 1);

        // 폴더별 모든 스크랩 조회
        List<AllScrap> recentScraps = userService.getScrapFolders(userId);

        // 특정 타입으로 필터링된 스크랩 목록
        List<AllScrap> productScraps = allScraps.stream()
                .filter(scrap -> "Product".equals(scrap.getType()))
                .collect(Collectors.toList());
        List<AllScrap> communityScraps = allScraps.stream()
                .filter(scrap -> "Community".equals(scrap.getType()))
                .collect(Collectors.toList());

        // 각 카테고리별 스크랩 개수 계산
        int productCount = productScraps.size();
        int communityCount = communityScraps.size();
        int totalScrapCount = allScraps.size();

        // 기본 폴더 식별
        AllScrap defaultFolder = recentScraps.stream()
                .filter(folder -> "기본 폴더".equals(folder.getFolderName()))
                .findFirst()
                .orElse(null);
        int defaultFolderId = defaultFolder != null ? defaultFolder.getFolderId() : -1;

        // 모델에 데이터 추가
        model.addAttribute("productScraps", productScraps);
        model.addAttribute("communityScraps", communityScraps);
        model.addAttribute("allScraps", allScraps);
        model.addAttribute("recentScraps", recentScraps);
        model.addAttribute("productCount", productCount);
        model.addAttribute("communityCount", communityCount);
        model.addAttribute("totalScrapCount", totalScrapCount);
        model.addAttribute("defaultFolderId", defaultFolderId);

        // 스크랩북 페이지로 이동
        return "user/mypage-scrapbook1";
    }

    @Operation(summary = "스크랩 폴더 상세", description = "마이페이지 스크랩 폴더 상세를 조회한다.")
    @GetMapping("/scrapbook1/{folderId}")
    public String getAllScrapsByFolder(@PathVariable("folderId") int folderId, Principal principal, Model model) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        // 특정 폴더에 속한 모든 스크랩 조회
        List<AllScrap> allScraps = userService.getAllScrapsByFolderId(userId, folderId);

        // 전체 폴더 목록 조회
        List<AllScrap> allFolders = userService.getScrapFolders(userId);

        // 기본 폴더 식별
        List<AllScrap> recentScraps = userService.getScrapFolders(userId);
        AllScrap defaultFolder = recentScraps.stream()
                .filter(folder -> "기본 폴더".equals(folder.getFolderName()))
                .findFirst()
                .orElse(null);
        boolean isDefaultFolder = (defaultFolder != null && folderId == defaultFolder.getFolderId());

        // Folder 타입은 제외하고 실제 스크랩만 필터링
        List<AllScrap> actualScraps = allScraps.stream()
                .filter(scrap -> !"Folder".equals(scrap.getType()))
                .collect(Collectors.toList());

        // 상품과 커뮤니티로 각각 필터링
        List<AllScrap> productScraps = actualScraps.stream()
                .filter(scrap -> "Product".equals(scrap.getType()))
                .collect(Collectors.toList());

        List<AllScrap> communityScraps = actualScraps.stream()
                .filter(scrap -> "Community".equals(scrap.getType()))
                .collect(Collectors.toList());

        // 각 카테고리별 수
        int productCount = productScraps.size();
        int communityCount = communityScraps.size();
        int totalScrapCount = actualScraps.size();

        // 모델에 추가
        model.addAttribute("isDefaultFolder", isDefaultFolder);
        model.addAttribute("allFolders", allFolders);
        model.addAttribute("scraps", allScraps); // 전체 데이터 (폴더 정보 포함)
        model.addAttribute("productScraps", productScraps); // 상품
        model.addAttribute("communityScraps", communityScraps); // 커뮤니티
        model.addAttribute("productCount", productCount);
        model.addAttribute("communityCount", communityCount);
        model.addAttribute("totalScrapCount", totalScrapCount); // 모든 스크랩의 실제 개수
        model.addAttribute("folderId", folderId); // folderId 추가
        model.addAttribute("user", user);

        return "user/mypage-scrapbook-folder";
    }

    /**
     * 마이페이지 - 스크랩북(상품)
     *
     * @return
     */
    @GetMapping("/scrapbook2")
    public String scrapbook2(@RequestParam(name = "catId", required = false, defaultValue = "0") int categoryId, Principal principal, Model model) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        int id = user.getId();

        // 모든 스크랩 조회
        List<AllScrap> allScraps = userService.getAllScraps(id, 1);

        // 타입별로 필터링된 스크랩 목록을 모델에 추가
        List<AllScrap> productScraps = allScraps.stream()
                .filter(scrap -> "Product".equals(scrap.getType()))
                .collect(Collectors.toList());
        List<AllScrap> communityScraps = allScraps.stream()
                .filter(scrap -> "Community".equals(scrap.getType()))
                .collect(Collectors.toList());

        model.addAttribute("productScraps", productScraps);
        model.addAttribute("communityScraps", communityScraps);
        model.addAttribute("allScraps", allScraps);

        List<UserProductScrap> scrappedProducts = userService.getProductScraps(id, categoryId);
        List<ProductCategory> categories = productService.getProductMainCategories();
        List<AllScrap> allFolders = userService.getScrapFolders(id);

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("scrappedProducts", scrappedProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("allFolders", allFolders);

        return "user/mypage-scrapbook2";
    }

    /**
     * 마이페이지 - 스크랩북(커뮤니티)
     *
     * @return
     */
    @GetMapping("/scrapbook3")
    public String scrapbook3(@RequestParam(name = "catId", required = false, defaultValue = "0") int categoryId, Principal principal, Model model) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);

        int id = user.getId();

        List<UserCommScrap> scrappedCommunities = userService.getCommunityScraps(id, categoryId);
        List<CommunityCategory> categories = communityService.getAllCommCategories();
        List<AllScrap> allFolders = userService.getScrapFolders(id);

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("scrappedCommunities", scrappedCommunities);
        model.addAttribute("categories", categories);
        model.addAttribute("allFolders", allFolders);

        return "user/mypage-scrapbook3";
    }

    @Operation(summary = "스크랩 폴더 삭제", description = "스크랩 폴더들을 삭제한다.")
    @PostMapping("/scrapbook1/deleteFolders")
    public String deleteScrapFolders(@RequestParam("folderIds") String folderIds, Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        // 쉼표로 구분된 폴더 ID들을 리스트로 변환
        List<Integer> folderIdList = Arrays.stream(folderIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // 각 폴더에 대해 삭제 및 이동 처리
        for (int folderId : folderIdList) {
            userService.deleteAndMoveScrapFolder(userId, folderId);
        }

        return "redirect:/user/scrapbook1";
    }

    @Operation(summary = "스크랩 아이템 이동", description = "선택된 스크랩 아이템을 선택된 폴더로 이동시킨다.")
    @PostMapping("/scrapbook/moveItems")
    public String moveScrapItems(
            @RequestParam("itemIds") String itemIdsCsv,
            @RequestParam("targetFolderId") int folderId,
            @RequestParam("types") String typesCsv,
            Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        // 콤마로 구분된 문자열을 개별 요소의 리스트로 분할
        String[] itemIds = itemIdsCsv.split(",");
        String[] types = typesCsv.split(",");

        for (int i = 0; i < itemIds.length; i++) {
            int itemId = Integer.parseInt(itemIds[i]);
            String type = types[i];
            userService.modifyScrapItemToFolder(userId, itemId, folderId, type);
        }

        return "redirect:/user/scrapbook";
    }

    @Operation(summary = "스크랩 아이템 삭제", description = "선택된 스크랩 아이템을 삭제한다.")
    @PostMapping("/scrapbook/deleteItems")
    public String deleteScrapItems(
            @RequestParam("itemIds") String itemIdsCsv,
            @RequestParam("types") String typesCsv,
            Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        String[] itemIds = itemIdsCsv.split(",");
        String[] types = typesCsv.split(",");

        for (int i = 0; i < itemIds.length; i++) {
            int itemId = Integer.parseInt(itemIds[i]);
            String type = types[i];
            userService.deleteScrapItem(userId, itemId, type);
        }

        return "redirect:/user/scrapbook";
    }

    @Operation(summary = "스크랩 폴더 수정", description = "스크랩 폴더명 및 설명을 수정한다.")
    @PostMapping("/scrapbook1/updateFolder")
    public ResponseEntity<String> modifyScrapFolder(
            @Valid @ModelAttribute FolderEdit folderEdit,
            @RequestParam("folderId") int folderId,
            Principal principal) {

        // 현재 사용자의 이메일로 유저 ID 가져오기
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        // 폴더 수정 서비스 호출
        userService.modifyScrapFolder(folderId, userId, folderEdit.getFolderName(), folderEdit.getFolderDescription());
        return ResponseEntity.ok("폴더가 수정되었습니다.");
    }

    @Operation(summary = "스크랩 폴더 추가", description = "새로운 스크랩 폴더를 생성한다.")
    @PostMapping("/scrapbook1/insertFolder")
    public ResponseEntity<String> addScrapFolder(
            @RequestParam("folderName") String folderName,
            @RequestParam("folderDescription") String folderDescription,
            Principal principal) {

        // 현재 사용자의 이메일로 유저 ID 가져오기
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        // 새로운 폴더 추가 서비스 호출
        userService.addScrapFolder(userId, folderName, folderDescription);
        return ResponseEntity.ok("새로운 폴더가 추가되었습니다.");
    }

    @Operation(summary = "새 폴더 생성 및 스크랩 아이템 이동", description = "새 폴더를 생성하고 아이템을 이동시킨다.")
    @PostMapping("/scrapbook/insertAndMove")
    public String addFolderAndMoveItems(
            @RequestParam("folderName") String folderName,
            @RequestParam("itemIds") String itemIdsCsv,
            @RequestParam("types") String itemTypesCsv,
            Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        // 새로운 폴더 생성 후 ID 가져오기
        Integer newFolderId = userService.addScrapFolderReturningId(userId, folderName);

        // 콤마로 구분된 문자열을 개별 요소의 리스트로 분할
        String[] itemIds = itemIdsCsv.split(",");
        String[] itemTypes = itemTypesCsv.split(",");

        // 각 아이템을 새 폴더로 이동
        for (int i = 0; i < itemIds.length; i++) {
            int itemId = Integer.parseInt(itemIds[i]);
            String itemType = itemTypes[i];

            // 이동 로직 수행
            userService.modifyScrapItemToFolder(userId, itemId, newFolderId, itemType);
        }

        // 새 폴더 페이지로 리다이렉트
        return "redirect:/user/scrapbook1/" + newFolderId;
    }

    /**
     * 마이페이지 - 좋아요
     *
     * @return
     */
    @GetMapping("/heart")
    public String heart(Model model, Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        int userId = user.getId();

        // 유저의 모든 좋아요 조회
        List<LikeDto> userLikes = userService.getUserLikes(userId);
        model.addAttribute("userLikes", userLikes);
        model.addAttribute("user", user);

        int totalScrapCount = userService.getTotalScrapCount(userId);
        model.addAttribute("totalScrapCount", totalScrapCount);

        int totalLikes = userService.getTotalLikes(userId);
        model.addAttribute("totalLikes", totalLikes);

        return "user/mypage-heart";
    }

    // 마이페이지 답변완료 문의내역
    @GetMapping("/myqna/answer")
    @PreAuthorize("isAuthenticated()")
    public String myqnaAnswer(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                              @Login LoginUser loginUser,
                              Model model) {
        int userId = loginUser.getId();

        int totalRows = qnaService.getTotalQnaRowsByUserId(userId, "Y");

        Pagination pagination = new Pagination(page, totalRows, 5);

        List<Qna> answerQnaList = qnaService.getAnswerQnasByUserId(userId, pagination);

        model.addAttribute("answerQnaList", answerQnaList);
        model.addAttribute("paging", pagination);

        return "user/mypage-qna-answer";
    }

    // 마이페이지 미답변 문의내역
    @GetMapping("/myqna/noAnswer")
    @PreAuthorize("isAuthenticated()")
    public String myqnaNoanswer(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                @Login LoginUser loginUser,
                                Model model) {
        int userId = loginUser.getId();

        int totalRows = qnaService.getTotalQnaRowsByUserId(userId, "N");

        Pagination pagination = new Pagination(page, totalRows, 5);

        List<Qna> noAnswerQnaList = qnaService.getNoAnswerQnasByUserId(userId, pagination);

        model.addAttribute("noAnswerQnaList", noAnswerQnaList);
        model.addAttribute("paging", pagination);

        return "user/mypage-qna-noanswer";
    }

    // 문의내역 삭제
    @GetMapping("/myqna/delete/{type}/{qnaId}")
    @PreAuthorize("isAuthenticated()")
    public String qnaDelete(@PathVariable("qnaId") int qnaId,
                            @PathVariable("type") String type,
                            @Login LoginUser loginUser,
                            RedirectAttributes redirectAttributes) {
        Qna qna = qnaService.getQnaById(qnaId);
        if (loginUser.getId() != qna.getUser().getId()) {
            throw new RuntimeException("다른 사용자의 문의사항은 삭제할 수 없습니다.");
        }
        qnaService.deleteQna(qnaId);

        redirectAttributes.addFlashAttribute("message", new Message("문의내역이 삭제 되었습니다."));

        return "redirect:/user/myqna/" + type;
    }

    // 마이페이지 포인트내역
    @GetMapping("/point")
    @PreAuthorize("isAuthenticated()")
    public String point(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                        @Login LoginUser loginUser,
                        Model model) {

        int userId = loginUser.getId();

        int totalRows = userService.getTotalPointHistory(userId);

        Pagination pagination = new Pagination(page, totalRows, 5);

        List<PointHistoryDto> pointHistoryList = userService.getPointHistoryByUserId(userId, pagination);

        User user = userMapper.getUserById(userId);

        model.addAttribute("balance", user.getPoint());
        model.addAttribute("pointHistoryList", pointHistoryList);
        model.addAttribute("paging", pagination);

        return "user/mypage-point";
    }

    // 비밀번호 변경 폼 페이지로 이동
    @GetMapping("/changePwd")
    public String showPasswordChangeForm(Model model) {
        model.addAttribute("passwordChangeForm", new PasswordChangeForm());
        return "user/mypage-resetpassword";
    }

    // 비밀번호 변경 요청 처리
    @PostMapping("/changePwd")
    public String handleChangePassword(@Valid @ModelAttribute PasswordChangeForm form, BindingResult errors, Principal principal) {
        // 현재 로그인한 사용자 이메일 가져오기
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        String userId = String.valueOf(user.getId());

        // 서버 측 유효성 검사
        if (errors.hasErrors()) {
            return "/user/password-change";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "user/password-change";
        }

        // 현재 비밀번호 확인
        if (!userService.checkCurrentPassword(userId, form.getCurrentPassword())) {
            errors.rejectValue("currentPassword", "error.currentPassword", "현재 비밀번호가 일치하지 않습니다.");
            return "user/password-change";
        }

        // 새 비밀번호 업데이트
        boolean success = userService.updatePassword(userId, form.getNewPassword());
        if (success) {
            return "redirect:/user/mypage";
        } else {
            // 업데이트 실패 시 처리
            return "redirect:/user/password-change";
        }
    }

    /**
     * 마이페이지 - 설정 조회
     *
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/setting")
    public String settingForm(Model model, Principal principal) {
        String email = principal != null ? principal.getName() : null;
        User user = userService.getUserByEmail(email);
        UserSettingForm userSettingForm = new UserSettingForm();

        userSettingForm.setEmail(email);
        userSettingForm.setNickname(user.getNickname());
        userSettingForm.setProfilePhotoUrl(user.getProfilePhoto() != null ? user.getProfilePhoto() : "/images/mypage/default.jpg");

        int userPoint = user.getPoint();
        model.addAttribute("userPoint", userPoint);

        model.addAttribute("userSettingForm", userSettingForm);
        return "user/settingform";
    }

    @PostMapping("/setting")
    public String updateSettings(@ModelAttribute UserSettingForm form,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        if (principal == null) {
            return "redirect:/user/login"; // 로그인하지 않은 사용자를 로그인 페이지로 리다이렉션
        }
        try {
            // 사용자 설정 업데이트 서비스 호출
            userService.modifyUserSettings(form, principal.getName());

            // 성공 메시지를 리다이렉션 속성에 추가
            redirectAttributes.addFlashAttribute("successMessage", "회원 정보가 성공적으로 업데이트되었습니다.");
            return "redirect:/user/mypage"; // 성공 시 마이페이지 메인
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/user/setting"; // 실패 시 회원정보 수정 페이지
        }
    }

    @Operation(summary = "인증인가 예외처리", description = "인증,인가 과정에서 문제발생시 오류페이지로 이동한다.")
    @GetMapping("/accessdenied")
    public String accessDenied() {
        return "/user/access-denied";
    }

    @Operation(summary = "이메일 중복 검사", description = "입력된 이메일이 기존에 등록된 이메일인지 검사한다.")
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        boolean isExist = userService.isEmailAvailable(email);
        return ResponseEntity.ok(Collections.singletonMap("exists", isExist));
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 처리한다.")
    @PostMapping("/withdraw")
    public String withdrawUser(Principal principal) {
        String email = principal != null ? principal.getName() : null;

        if (email != null) {
            userService.withdrawUser(email);
        }

        return "redirect:/user/logout";
    }

    @Operation(summary = "팔로우 추가", description = "다른 유저를 팔로우한다.")
    @PostMapping("/addFollow")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> createCommunityScrap(@RequestParam("followeeUserId") int followeeUserId,
                                                     @Login LoginUser loginUser) {
        userService.addFollow(loginUser.getId(), followeeUserId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "팔로우 삭제", description = "커뮤니티글을 스크랩 삭제(취소)한다.")
    @PostMapping("/deleteFollow")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCommunityScrap(@RequestParam("followeeUserId") int followeeUserId,
                                                     @Login LoginUser loginUser) {
        userService.deleteFollow(loginUser.getId(), followeeUserId);

        return ResponseEntity.ok().build();
    }
}