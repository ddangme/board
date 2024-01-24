package com.ddangme.board.repository;

import com.ddangme.board.domain.ArticleComment;
import com.ddangme.board.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository
        extends JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_id(Long articleId);

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // 전체 검색 하지 않을 것
        bindings.excludeUnlistedProperties(true);

        // 검색을 허용할 필드 선택
        bindings.including(root.content, root.createdAt, root.createdBy);

        // 검색 파라미터는 하나만 받겠다.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}