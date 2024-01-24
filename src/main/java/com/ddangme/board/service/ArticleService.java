package com.ddangme.board.service;

import com.ddangme.board.domain.type.SearchType;
import com.ddangme.board.dto.ArticleDto;
import com.ddangme.board.dto.ArticleUpdateDto;
import com.ddangme.board.dto.ArticleWithCommentsDto;
import com.ddangme.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        return Page.empty();
    }

    public ArticleWithCommentsDto getArticle(Long articleId) {
        return null;
    }


    @Transactional(readOnly = false)
    public void saveArticle(ArticleDto dto) {
    }

    @Transactional(readOnly = false)
    public void updateArticle(ArticleDto dto) {
    }

    @Transactional(readOnly = false)
    public void deleteArticle(long articleId) {
    }
}
