package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import utils.IdWorker;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    public Page<Spit> searchPage(String content,int page, int size) {
    return spitDao.findByContentLike(content,new PageRequest(page-1, size, Sort.Direction.DESC,"_id"));
    // return  spitDao.findByContentLike(PageRequest.of(page-1, size, Sort.Direction.DESC,"_id"));
    }

    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    public Spit findById(String spitId) {
        return spitDao.findById(spitId).get();
    }

    public void add(Spit spit) {
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        if (!StringUtils.isEmpty(spit.getParentid())){
            //是评论,回复数+1
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update=new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "spit");
        }
        spitDao.save(spit);
    }

    public void update(Spit spit,String spitId) {
        spit.set_id(spitId);
        spitDao.save(spit);
    }

    public void deleteById(String spitId) {
     spitDao.deleteById(spitId);
    }

    public Page<Spit> findByParentid(String parentid, int page, int size) {
      Page<Spit> spitPage= spitDao.findByParentid(parentid,new PageRequest(page-1, size, Sort.Direction.DESC,"_id"));
        return spitPage;
    }

    public void updatethumbup(String spitId,String userId) {
        /*Spit spit = spitDao.findById(spitId).get();
        spit.setThumbup(spit.getThumbup()+1);
        spitDao.save(spit);*/
        //在redis中判断是否点过赞
        Object o = redisTemplate.boundValueOps(userId + spitId).get();
        if (o!=null){
            throw new RuntimeException("已经点过赞了,不能重复点赞");
        }
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update=new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, "spit");
        redisTemplate.boundValueOps(userId + spitId).set(1);
    }
}
