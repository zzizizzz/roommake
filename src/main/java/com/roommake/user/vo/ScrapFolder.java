package com.roommake.user.vo;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrapFolder {

    private int id;             // 스크랩 폴더 번호
    private String name;        // 스크랩 폴더 이름
    private String description; // 스크랩 폴더 설명
    private User userId;        // 유저 번호
}
