package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {
    //private static final Logger log = (Logger) LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    /**
     * 发送短信验证码
     */
    public void createSmsCode(String mobile) {
        // 1. 判断是否已经发送过验证码了，验证码未失效.
        String code = (String) redisTemplate.boundValueOps(mobile).get();
        if (!StringUtils.isEmpty(code)) {
            throw new RuntimeException("验证码已经发送过了，请注意查收!");
        }
        //2.生成6位数的验证码
        code = RandomStringUtils.randomNumeric(6);

        System.out.println("手机号:" + mobile + "  " + "验证码:" + code);
        //3.保存到redis中
        redisTemplate.boundValueOps(mobile).set(code, 5, TimeUnit.MINUTES);
        // 4.写入消息队列,消息内容 map 手机号,验证码
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("code", code);
        rabbitTemplate.convertAndSend("sms", map);

    }

    public void register(User user, String code) {
        //判断验证码是否成功
        String redisCode = (String) redisTemplate.boundValueOps(user.getMobile()).get();
        if (!redisCode.equals(code)) {
            throw new RuntimeException("您输入的验证码不正确!请核对后在输入...");
        }

        user.setId(idWorker.nextId() + "");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		/*Date date=new Date();
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(date);//注册日期
		user.setUpdatedate( date);//更新日期
		user.setLastdate( date);//最后登陆日期*/
        userDao.save(user);
    }

    /**
     * 查询数据库里面的用户名登录
     *
     * @param mobile
     * @param password
     * @return
     */

    public User findByMobileAndPassword(String mobile, String password) {
        User user = userDao.findByMobile(mobile);
        //第一个参数是明文密码,没加密的,第二个参数是加密后的密码
        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("admin");
        boolean b = bCryptPasswordEncoder.matches("admin", encode);
        System.out.println(b);


        String str = DigestUtils.md5DigestAsHex("123".getBytes());
        for (int i = 0; i < 10; i++) {
            str= DigestUtils.md5DigestAsHex(str.getBytes());
        }
        System.out.println(str);
        //System.out.println(new Md5Hash("123", "abc", 100));
        System.out.println(new BCryptPasswordEncoder().encode("123"));
    }


    /**
     * 修改用户的粉丝数
     *
     * @param userId
     * @param num
     */
    public void updateFansCount(String userId, int num) {
        User user = userDao.findById(userId).get();
        user.setFanscount(user.getFanscount() + num);
        userDao.save(user);
    }

    /**
     * 修改关注数
     *
     * @param userId
     * @param num
     */
    public void updateFollowCount(String userId, int num) {
        User user = userDao.findById(userId).get();
        user.setFollowcount(user.getFollowcount() + num);
        userDao.save(user);
    }
}
