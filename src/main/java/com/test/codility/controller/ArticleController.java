package com.test.codility.controller;

import com.test.codility.dto.ArticleDTO;
import com.test.codility.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO) {
        ArticleDTO createdArticle = articleService.createArticle(articleDTO);
        return ResponseEntity.ok(createdArticle);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> findTitles(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<ArticleDTO> articleDTOs = articleService.findTitles(title, limit);
        return ResponseEntity.ok(articleDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        ArticleDTO updatedArticle = articleService.updateArticle(id, articleDTO);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
