package com.twinkle.community.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;


}
