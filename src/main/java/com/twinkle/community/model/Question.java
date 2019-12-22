package com.twinkle.community.model;

import lombok.Data;

@Data
public class Question {

    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtUpdate;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;


}
