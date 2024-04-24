package com.roommake.channel.controller;

import com.roommake.channel.dto.ChannelDto;
import com.roommake.channel.dto.PostForm;
import com.roommake.channel.service.PostService;
import com.roommake.channel.vo.ChannelPost;
import com.roommake.utils.S3Uploader;
import com.roommake.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/channel/post")
@RequiredArgsConstructor
@Tag(name = "Post API", description = "각 채널의 글에 대한 추가,변경,삭제,조회 API를 제공한다.")
public class PostController {

    private final PostService postService;
    private final S3Uploader s3Uploader;

    @Operation(summary = "채널에 대한 전체글 조회", description = "해당 채널에 대한 채널정보, 전체글, 참가여부를 조회한다.")
    @GetMapping("/list/{channelId}")
    public String postList(@PathVariable("channelId") int channelId, Model model, Principal principal) {

        String email = principal != null ? principal.getName() : null;
        ChannelDto dto = postService.getAllPostsByChannelId(channelId, email);

        model.addAttribute("channel", dto.getChannel());
        model.addAttribute("postList", dto.getChannelPosts());
        model.addAttribute("participant", true);
        model.addAttribute("participantCount", dto.getChannelParticipantCount());
        model.addAttribute("postCount", dto.getChannelPostCount());

        return "channel/post/list";
    }

    @Operation(summary = "채널글 등록폼", description = "채널글 등록폼를 조회한다.")
    @GetMapping(path = "/create/{channelId}")
    public String createForm(@PathVariable("channelId") int channelId, Model model) {
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("channelId", channelId);

        return "channel/post/form";
    }

    @Operation(summary = "채널글 등록", description = "채널글을 추가한다.")
    @PostMapping(path = "/create/{channelId}")
    public String createPost(@PathVariable("channelId") int channelId, @Valid PostForm postForm, BindingResult errors) {
        if (errors.hasErrors()) {
            return "channel/post/form";
        }
        String s3Url = s3Uploader.saveFile(postForm.getImageFile());
        postService.createPost(channelId, postForm, s3Url);

        return "redirect:/channel/post/list/{channelId}";
    }

    @GetMapping("/detail/{postId}")
    public String detailPost(@PathVariable("postId") int postId, Model model) {
        ChannelPost post = postService.getPostByPostId(postId);
        String addBrContent = StringUtils.withBr(post.getContent());
        post.setContent(addBrContent);
        model.addAttribute("post", post);
        return "channel/post/detail";
    }

    @Operation(summary = "채널글 수정폼", description = "채널글 수정폼를 조회한다.")
    @GetMapping(path = "/modify/{postId}")
    public String modifyForm(@PathVariable("postId") int postId, Model model) {
        ChannelPost post = postService.getPostByPostId(postId);
        PostForm postForm = PostForm.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
        String imageName = post.getImageName();
        model.addAttribute("postForm", postForm);
        model.addAttribute("imageName", imageName);
        model.addAttribute("postId", postId);

        return "channel/post/form";
    }

    @Operation(summary = "채널글 수정", description = "채널글을 수정한다.")
    @PostMapping(path = "/modify/{postId}")
    public String modifyPost(@PathVariable("postId") int postId, @Valid PostForm postForm, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "channel/post/form";
        }
        ChannelPost post = postService.getPostByPostId(postId);
        String image = "";
        if (postForm.getImageFile() != null) {
            image = s3Uploader.saveFile(postForm.getImageFile());
        }
        postService.modifyPost(postForm, image, post);
        return "redirect:/channel/post/detail/{postId}";
    }

    @GetMapping(path = "/delete/{postId}")
    public String deletePost(@PathVariable("postId") int postId) {
        ChannelPost post = postService.getPostByPostId(postId);
        int channelId = post.getChannel().getId();
        postService.deletePost(post);
        return String.format("redirect:/channel/post/list/%d", channelId);
    }
}
