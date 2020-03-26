package com.tensquare.article.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tensquare.article.dao.OptionDao;
import com.tensquare.article.dao.SpectionDao;
import com.tensquare.article.jiekou.SpectionServince;
import com.tensquare.article.pojo.Option;
import common.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.service
 * @date: 2020-02-09 00:06:34
 * @describe:
 */
@Component
public class SpectionRunnable implements Callable {
    private static final Logger logger = LoggerFactory.getLogger(SpectionRunnable.class);
    //private SpectionServince spectionServince;
     @Autowired
    private OptionDao optionDao;
    private static Object object;
    private Integer pageNum;
    private Integer pageSize;

    public SpectionRunnable() {
    }

    public SpectionRunnable( Integer pageNum, Integer pageSize) {


        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public Object call() throws Exception {

        PageBean<Option> pageBean = getOptionPageBean();
        return pageBean;
    }

    private PageBean<Option> getOptionPageBean() {

            PageBean<Option> pageBean = new PageBean<>();
            /** 查询总记录数 */
            Integer tatolCount = optionDao.getTatolCount();
            logger.info("tatolCount{}:"+tatolCount);
            Integer index = (pageNum - 1) * pageSize;
            logger.info("index{}:"+index);
            List<Option> optionList = optionDao.selectPage(index, pageSize);
            //Integer totalPage=(int) Math.ceil(tatolCount*1.0/pageSize);
            Integer totalPage = tatolCount % pageSize == 0 ? tatolCount % pageSize : tatolCount / pageSize + 1;
            pageBean.setPageNum(pageNum);
            pageBean.setPageSize(pageSize);
            pageBean.setTatolCount(tatolCount);
            pageBean.setTotalPage(totalPage);
            pageBean.setData(optionList);
            logger.info("SpectionRunnable{}:" + JSON.toJSONString(pageBean));
            return pageBean;

    }
}
