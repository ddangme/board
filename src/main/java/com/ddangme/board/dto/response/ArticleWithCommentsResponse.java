package com.ddangme.board.dto.response;

import com.ddangme.board.dto.ArticleCommentDto;
import com.ddangme.board.dto.ArticleWithCommentsDto;
import com.ddangme.board.dto.HashtagDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String title,
        String content,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId,
        Set<ArticleCommentResponse> articleCommentsResponse
) {

    public static ArticleWithCommentsResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, String userId, Set<ArticleCommentResponse> articleCommentResponses) {
        return new ArticleWithCommentsResponse(id, title, content, hashtags, createdAt, email, nickname, userId, articleCommentResponses);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toUnmodifiableSet()),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId(),
                organizeChildComments(dto.articleCommentDtos())
        );
    }

    private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> dtos) {

        // 'dtos' 라는 'ArticleCommentDto' 객체들로 이루어진 집합을 스트림으로 변환한다.
        // 각 'ArticleCommentDto'를 'ArticleCommentResponse'로 변환하여, 해당 객체들의 ID를 key로 하는 'Map'으로 수집한다.
        // Function.identity(): ArticleCommentResponse 값 그대로 저장한다.
        Map<Long, ArticleCommentResponse> map = dtos.stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toMap(ArticleCommentResponse::id, Function.identity()));

        // 부모 댓글을 가진 댓글을 찾아 부모 댓글의 'childComments'에 자식 댓글을 추가한다.
        map.values().stream()
                .filter(ArticleCommentResponse::hasParentComment)
                .forEach(comment -> {
                    ArticleCommentResponse parentComment = map.get(comment.parentCommentId());
                    parentComment.childComments().add(comment);
                });

        // 부모 댓글이 없는 (자식이 아닌) 댓글들을 필터링하여, 이 댓글들을 최종적으로 정리된 형태의 Set으로 반환한다.
        // 반환되는 Set은 'TreeSet'으로, 'Comparator'를 이용하여 댓글을 정렬한다.
        // 먼저, 'createdAt'을 기준으로 내림차순으로 정렬하고, 그 후에 'id'를 기준으로 정렬한다.
        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(ArticleCommentResponse::createdAt)
                                .reversed()
                                .thenComparingLong(ArticleCommentResponse::id)
                        )
                ));
    }


}