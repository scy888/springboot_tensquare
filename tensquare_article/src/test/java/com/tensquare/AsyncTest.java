package com.tensquare;

import com.tensquare.article.ArticleApplication;
import com.tensquare.article.controller.AsyncDome;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare
 * @date: 2020-03-07 22:04:27
 * @describe: 异步任务测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ArticleApplication.class)
public class AsyncTest {
    @Autowired
    private AsyncDome asyncDome;
    @Test
    public void acyncTest() throws Exception {
        asyncDome.doTaskOne();
        asyncDome.doTaskTwo();
        asyncDome.doTaskThree();
    }
    @Test
    public void scheduledTset() throws Exception {
        asyncDome.doScheduled();
    }
}
