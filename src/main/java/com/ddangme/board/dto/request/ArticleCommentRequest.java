package com.ddangme.board.dto.request;

import com.ddangme.board.dto.ArticleCommentDto;
import com.ddangme.board.dto.UserAccountDto;

/**
 * DTO for {@link com.ddangme.board.domain.ArticleComment}
 */
public record ArticleCommentRequest(Long articleId, String content) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                content
        );
    }

}