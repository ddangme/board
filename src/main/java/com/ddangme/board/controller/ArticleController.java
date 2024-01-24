package com.ddangme.board.controller;

import com.ddangme.board.domain.type.SearchType;
import com.ddangme.board.dto.response.ArticleResponse;
import com.ddangme.board.dto.response.ArticleWithCommentsResponse;
import com.ddangme.board.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public String articles(@RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchKeyword,
                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                           ModelMap map) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchKeyword, pageable).map(ArticleResponse::from);

        map.addAttribute("articles", articles);

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
        ArticleWithCommentsResponse result = ArticleWithCommentsResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", result);
        map.addAttribute("articleComments", result.articleCommentsResponse());

        return "articles/detail";
    }

}