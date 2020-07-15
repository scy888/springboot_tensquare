package com.tensquare.test;

import com.tensquare.test.dao.UserDomeDao;
import com.tensquare.test.pojo.User;
import common.JacksonUtils;
import common.NumUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
@AutoConfigureMockMvc
public class DomeTest {
    @Autowired
    private UserDomeDao userDomeDao;
    @Autowired
    private JacksonUtils jacksonUtils;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void init(){
        mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test
    public void test01(){
        List<User> userList = userDomeDao.getList("男");
        log.info("userList:{}",jacksonUtils.toString(userList));
    }
}
