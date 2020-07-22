package com.tensquare.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensquare.test.dao.UserDomeDao;
import com.tensquare.test.pojo.User;
import com.tensquare.test.pojo.UserDto;
import common.JacksonUtils;
import common.NumUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Test
    public void test_() throws Exception{
        List<UserDto> userDtoList = Arrays.asList(new UserDto[]{new UserDto().setName("无忌").setSex("男").setAge(20).setContext("倚天屠龙记"),
                new UserDto().setName("芷若").setSex("女").setAge(19).setContext("倚天屠龙记")});
        String value = new ObjectMapper().writeValueAsString(userDtoList);
        String contentAsString = mockMvc.perform(MockMvcRequestBuilders.post("/localhost:9023/batch/userDome/addUserDtoList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(value)).andReturn().getResponse().getContentAsString();
        log.info("contentAsString:{}",jacksonUtils.toString(contentAsString));
    }
    @Test
    public void testInsetList(){
        List<UserDto> userDtoList = Arrays.asList(new UserDto[]{new UserDto("张无忌", "男", 20, LocalDateTime.of(2012, 6, 12, 12, 12, 12)),
                new UserDto("赵敏", "女", 19, LocalDateTime.of(2012, 6, 12, 12, 12, 12)),
                new UserDto("周芷若", "女", 18, LocalDateTime.of(2012, 6, 12, 12, 12, 12)),
                new UserDto("小昭", "女", 17, LocalDateTime.of(2012, 6, 12, 12, 12, 12)),
                new UserDto("阿离", "女", 16, LocalDateTime.of(2012, 6, 12, 12, 12, 12))});
        //userDomeDao.insertUserDtoList(userDtoList);
        //userDomeDao.addUserList(userDtoList);
        userDomeDao.addList(userDtoList);
    }
}
