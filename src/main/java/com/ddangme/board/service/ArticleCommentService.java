package com.ddangme.board.service;

import com.ddangme.board.dto.ArticleCommentDto;
import com.ddangme.board.repository.ArticleCommentRepository;
import com.ddangme.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public List<ArticleCommentDto> searchArticleComment(Long articleId) {
        return List.of();
    }

    @Transactional(readOnly = false)
    public void saveArticleComment(ArticleCommentDto dto) {

    }

    @Transactional(readOnly = false)
    public void updateArticleComment(ArticleCommentDto dto) {

    }

    @Transactional(readOnly = false)
    public void deleteArticleComment(Long articleCommentId) {

    }
}
