package com.test.codility.repository;

import com.test.codility.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT a FROM Article a WHERE UPPER(a.title) LIKE UPPER(CONCAT('%', ?1, '%')) ORDER BY a.id DESC")
    List<Article> findTopByTitleContainingIgnoreCaseOrderByIdDesc(String title, int limit);
}
