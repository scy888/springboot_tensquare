package com.tensquare.article.service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.annotation.Rollback;
import com.tensquare.article.dao.SysLogDao;
import com.tensquare.article.pojo.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author: scyang
 * @program: tensquare_parent
 * @date: 2019-09-21 00:02:24
 */
@Service
//@Transactional
//@Rollback(false)
public class SysLogService {
    @Autowired
    private SysLogDao sysLogDao;

    public void addSysLog(SysLog sysLog) {
    sysLogDao.addSysLog(sysLog);
        //deleteSysLog(sysLog);
    }

    //@Scheduled(cron = "0 0 0 * * ? ")
    public void deleteSysLog() {
        sysLogDao.deleteSysLog();
    }

}
