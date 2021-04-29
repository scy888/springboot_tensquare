package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import utils.IdWorker;

@Service
public class ArticleSearchService {
    @Autowired
    private ArticleSearchDao articleSearchDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void add(Article article) {
        article.setId(idWorker.nextId() + "");
        articleSearchDao.save(article);

        //elasticsearchTemplate.queryForList(new SearchQuery(Criteria.where("").and()));
    }

    public Page<Article> findByContentLike(String keywords, int page, int size) {

        return articleSearchDao.findByContentLike(keywords, PageRequest.of(page - 1, size));
    }
}
