package com.tensquare.article.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tensquare.article.dao.ChannelDao;
import com.tensquare.article.pojo.Channel;
import com.tensquare.article.pojo.ListAndArray;
import entity.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class ChannelService {

    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public PageInfo<Channel> findAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Channel> channelList = channelDao.findAll();
        PageInfo<Channel> pageInfo=new PageInfo<>(channelList);
        return pageInfo;
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Channel> findSearch(Map whereMap, int page, int size) {
        Specification<Channel> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return channelDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Channel> findSearch(Map whereMap) {
        Specification<Channel> specification = createSpecification(whereMap);
        return channelDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    //@Cacheable(value = "channel", key = "#map.get('id')")
    public Channel findById(String id) {
        //String id= (String) map.get("id");
        //return channelDao.findById(id).get();
        return channelDao.findByMap(id);
    }

    /**
     * 增加
     *
     * @param channel
     */
    @CacheEvict(value = "channel", key = "#channel.id")
    public void add(Channel channel) {
        channel.setId(idWorker.nextId() + "");
        // channel.setName("湖南");
        channelDao.save(channel);
    }

    /**
     * 修改
     *
     * @param channel
     */
    @CacheEvict(value = "channel", key = "#channel.id")
    public void update(Channel channel) {
        channelDao.save(channel);
    }

    /**
     * 删除
     *
     * @param id
     */
    @CacheEvict(value = "channel", key = "#id")
    public void deleteById(String id) {
        channelDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Channel> createSpecification(Map searchMap) {

        return new Specification<Channel>() {

            @Override
            public Predicate toPredicate(Root<Channel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 频道名称
                if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%" + (String) searchMap.get("name") + "%"));
                }
                // 状态
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%" + (String) searchMap.get("state") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }


    //@CacheEvict(value = "channel")
    public void addMap(Map<String, List<Channel>> paramMap) {
        List<Channel> channls = paramMap.get("channels");
        String name = channls.get(0).getName();
        for (Channel channel : channls) {
            channel.setId(idWorker.nextId() + "");
            channel.setName(name);
            channelDao.save(channel);
        }
    }

    //@CacheEvict(value = "channel")
    public void addList(ListAndArray listAndArray) {
		/*for (Channel channel : listAndArray.getChannelList()) {
			channel.setId( idWorker.nextId()+"" );
			channelDao.save(channel);
		}*/
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(listAndArray));
        List<Channel> channelList = jsonObject.getJSONArray("channelList").toJavaList(Channel.class);
        for (Channel channel : channelList) {
            channel.setId(idWorker.nextId() + "");
            channelDao.save(channel);
        }
		/*List<Channel> channelList = (List<Channel>) jsonObject.get("channelList");
		for (Channel channel : channelList) {
			channel.setId( idWorker.nextId()+"" );
			channelDao.save(channel);//会报错
		}*/
    }

    //@CacheEvict(value = "channel")
    public void addArray(Channel[] channels) {
        for (Channel channel : channels) {
            channel.setId(idWorker.nextId() + "");
            channelDao.save(channel);
        }
    }

    //@CacheEvict(value = "channel")
    public void delById(String s) {
        channelDao.deleteById(s);
    }

    public List<Channel> selectByIsHot(String isHost) {
        List<Channel> channelList = null;
        if (Constant.IS_HOT.equals(isHost)) {
            channelList = channelDao.findTop9ByIsHotOrderByCreateDateDesc(isHost);
        }
        return channelList;
    }

    public void updateByThumbUp(String id) {
        String key = "userId" + id;
		/*Object o = redisTemplate.opsForValue().get(key);
		if (o==null){
			Channel channel = channelDao.findById(id).get();
			String thumbUp = channel.getThumbUp();
			if (thumbUp==null){
				thumbUp="0";
			}
			Integer integer = Integer.valueOf(thumbUp);
			integer=integer+1;
			channel.setThumbUp(String.valueOf(integer));
			channelDao.save(channel);
			redisTemplate.opsForValue().set(key, "", 10,TimeUnit.MINUTES);
		}
        else {
        	throw new RuntimeException("已经点过赞了,请过10分钟再来点....");
		}*/
        Integer num = (Integer) redisTemplate.opsForValue().get(key);
        if (num == null) {
            redisTemplate.opsForValue().set(key, 0, 1, TimeUnit.MINUTES);
        }
        Channel channel = channelDao.findById(id).get();
        String thumbUp = channel.getThumbUp();
        if (thumbUp == null) {
            thumbUp = "0";
        }
        Integer integer = Integer.valueOf(thumbUp);
        integer = integer + 1;
        channel.setThumbUp(String.valueOf(integer));
        channelDao.save(channel);
        redisTemplate.opsForValue().increment(key, 1);
        if (num == 5) {
            throw new RuntimeException("您已在1分钟内点赞的次数超过5次,请过1分钟后再来点赞....");
        }
    }


    public void addMapArray( Channel[] channelArray) {
        for (Channel channel : channelArray) {
            channel.setId(idWorker.nextId() + "");
            channelDao.save(channel);
        }
    }

    public List<Channel> selectByNameAndState(String name, String state) {

        return channelDao.findByNameAndState(name,state);
    }

    public List<Channel> selectByStateAndName(String name, String state) {

        return channelDao.selectByNameAndState(name,state );
    }

    public List<Channel> selectByCreateDateAndIsHot(Date createDate, String isHot) {

        return channelDao.findByCreateDateAndIsHot(createDate,isHot);
    }

    public  List<Channel> selectByName(String name) {
        return channelDao.findByName(name);
    }


	/*public void addMap(List<Channel> channelList) {
		for (Channel channel : channelList) {
			channel.setId( idWorker.nextId()+"" );
			channelDao.save(channel);
		}
	}*/
}
