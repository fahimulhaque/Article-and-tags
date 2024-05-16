package com.test.codility.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleDTO {

    private Long id;
    private String title;
    private String content;
    private List<String> tags;

    // Getters and Setters omitted for brevity
}