package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("article")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;
    ElasticsearchTemplate elasticsearchTemplate;

    @PostMapping
    public Result add(@RequestBody Article article) {
        articleSearchService.add(article);

        return Result.success("添加成功");
    }

    @GetMapping("/search/{keywords}/{page}/{size}")
    public Result findByContentLike(@PathVariable String keywords, @PathVariable int page, @PathVariable int size) {
        Page<Article> articlePage = articleSearchService.findByContentLike(keywords, page, size);
        return Result.success("查询成功", articlePage);
    }
}
