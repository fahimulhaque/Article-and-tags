package com.test.codility.service;

import com.test.codility.dto.ArticleDTO;
import com.test.codility.entity.Article;
import com.test.codility.entity.Tag;
import com.test.codility.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    private List<String> blacklist;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository, @Value("${articles.blacklist}") List<String> blacklist) {
        this.articleRepository = articleRepository;
        this.blacklist = blacklist;
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO) {
        validateContent(articleDTO.getContent());
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());

        List<Tag> tags = new ArrayList<>();
        for (String tag : articleDTO.getTags()) {
            tags.add(new Tag(article, tag));
        }
        article.setTags(tags);

        articleRepository.save(article);

        articleDTO.setId(article.getId());
        return articleDTO;
    }

    public ArticleDTO findById(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        ArticleDTO articleDTO = new ArticleDTO();
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            articleDTO.setId(article.getId());
            articleDTO.setTitle(article.getTitle());
            articleDTO.setContent(article.getContent());
            // Convert tags to list of strings for DTO
            articleDTO.setTags(article.getTags().stream().map(Tag::getTag).toList());
        }
        return articleDTO;
    }

    public List<ArticleDTO> findTitles(String title, int limit) {
        List<Article> articles;
        if (limit > 0) {
            articles = articleRepository.findTopByTitleContainingIgnoreCaseOrderByIdDesc(title, limit);
        } else {
            articles = articleRepository.findByTitleContainingIgnoreCase(title);
        }
        List<ArticleDTO> articleDTOs = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(article.getId());
            articleDTO.setTitle(article.getTitle());
            // Omit content and tags for efficiency when only titles are needed
            articleDTO.setContent(article.getContent());
            articleDTO.setTags(article.getTags().stream().map(Tag::getTag).toList());
            articleDTOs.add(articleDTO);
        }
        return articleDTOs;
    }

    public ArticleDTO updateArticle(Long id, ArticleDTO articleDTO) {
        validateContent(articleDTO.getContent());
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article existingArticle = optionalArticle.get();
            existingArticle.setTitle(articleDTO.getTitle());
            existingArticle.setContent(articleDTO.getContent());

            // Update tags if necessary
            List<Tag> existingTags = existingArticle.getTags();
            existingTags.clear(); // Remove existing tags
            for (String tag : articleDTO.getTags()) {
                existingTags.add(new Tag(existingArticle, tag));
            }

            articleRepository.save(existingArticle);

            articleDTO.setId(existingArticle.getId());
            return articleDTO;
        } else {
            throw new RuntimeException("Article not found with id: " + id);
        }
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    private void validateContent(String content) {
        if (content != null && !content.isEmpty()) {
            for (String word : content.split("\\W+")) {
                if (blacklist.contains(word.toLowerCase())) {
                    throw new RuntimeException("Article contains forbidden words");
                }
            }
        }
    }

}
