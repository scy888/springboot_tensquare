package com.tensquare.test;

import com.tensquare.test.dao.UserDomeDao;
import com.tensquare.test.pojo.User;
import common.JacksonUtils;
import common.NumUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test
 * @date: 2020-07-04 12:36:25
 * @describe:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DomeTest {
    @Autowired
    private UserDomeDao userDomeDao;
    @Autowired
    private JacksonUtils jacksonUtils;

    @Test
    public void test01(){
        List<User> userList = userDomeDao.getList("男");
        log.info("userList:{}",jacksonUtils.toString(userList));
    }
}
