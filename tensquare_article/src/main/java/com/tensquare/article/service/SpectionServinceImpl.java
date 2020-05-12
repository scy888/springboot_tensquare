package com.tensquare.article.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tensquare.article.dao.*;
import com.tensquare.article.jiekou.SpectionServince;
import com.tensquare.article.pingan.*;
import com.tensquare.article.pojo.*;
import common.*;
import entity.Constant;
import entity.Guarantee;
import entity.GuaranteeExt;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utils.IdWorker;

import javax.jms.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.service
 * @date: 2020-02-06 14:06:09
 * @describe:
 */
@Service
public class SpectionServinceImpl implements SpectionServince {
    private static final Logger logger = LoggerFactory.getLogger(SpectionServinceImpl.class);
    @Autowired
    private SpectionDao spectionDao;
    @Autowired
    private OptionDao optionDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private PolicyDao policyDao;
    @Autowired
    private GuaranteeDao guaranteeDao;
    @Autowired
    private GuaranteeExtDao guaranteeExtDao;
    @Autowired
    private InsuredDao insuredDao;
    @Autowired
    private ExtendDao extendDao;
    @Autowired
    private RiskBackDao riskBackDao;
    @Autowired
    private SalvageDao salvageDao;
    @Autowired
    private ProvinceDao provinceDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ConsultationDao consultationDao;
    @Autowired
    private EmailDao emailDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TeacherAndStudentDao teacherAndStudentDao;
    @Autowired
    private WeightDao weightDao;
    @Autowired
    private PreCaseDao preCaseDao;
    @Autowired
    private OutBreakDao outBreakDao;
    @Autowired
    private MsgNoticeDao msgNoticeDao;
    @Autowired
    private EmploverDao emploverDao;
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private DutyDao dutyDao;
    @Autowired
    private DutyPersonDao dutyPersonDao;
    @Autowired
    private CaseTeamDao caseTeamDao;
    @Autowired
    private CasePersonDao casePersonDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private NumUtils numUtils;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String OPTION_STATUS_ONE = "1";
    private static final String OPTION_STATUS_TWO = "2";
    private static final String REGISTER_URL = "register_url";
    private static final String captchaId = "";
    @Value("${ccb}")
    private String CCB;
    @Value("${abc}")
    private String ABC;
    @Value("${icbc}")
    private String ICBC;
    @Value("${cmb}")
    private String CMB;
    @Value("${imageKey}")
    private String IMAGEKEY;

    @Override
    public void addSpection(String paramJson) {
        /**
         * @Description: 添加规格列表（规格选项列表,一对多）
         * @methodName: addSpection
         * @Param: [paramJson]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/6 14:55
         */
        JSONObject jsonObject = JSON.parseObject(paramJson);
        Spection spection = jsonObject.toJavaObject(Spection.class);
        List<Option> optionList = jsonObject.getJSONArray("optionList").toJavaList(Option.class);
        spection.setSpectionId(idWorker.nextId() + "");
        logger.info("spection{}:" + JSON.toJSONString(spection));
        spectionDao.addSpection(spection);
       /* List<Option> optionList=new ArrayList<>();
        Option option=new Option();
        option.setOptionName("李白");
        option.setOptionStatus("0");
        option.setOptionDate(new Date(1529-1900,12-1,15,8,8,8));
         {"朝代":"明朝","作者":"罗贯中"}
        option.setOptionDesc("{\"朝代\":\"唐朝\",\"职业\":\"诗人\"}");
        optionList.add(option);*/
        setOptionIdAndSpectionId(spection, optionList);
        /** 批量添加规格选项列表 */
        optionDao.addOptionList(optionList);
        String s = "{\"朝代\":\"明朝\",\"作者\":\"罗贯中\"}";
        String s2 = "[{\"朝代\":\"清朝\",\"作者\":\"曹雪芹\"},{\"朝代\":\"清朝\",\"作者\":\"高鹗\"}]";
    }

    @Override
    public void deleteSpectionByIds(List<String> idList) {
        /**
         * @Description: 批量删除规格列表
         * @methodName: deleteSpectionByIds
         * @Param: [idList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/6 18:11
         */
        spectionDao.deleteSpectionByIds(idList);
        optionDao.deleteOptionByRelationId(idList);
    }

    @Override
    public void updateSpection(String paramJson) {
        /**
         * @Description: 修改规格列表
         * @methodName: updateSpection
         * @Param: [spection]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/6 18:42
         */
        Map<String, Object> map = JSON.parseObject(paramJson).toJavaObject(Map.class);
        logger.info("map{}:" + JSON.toJSONString(map));
        spectionDao.updateSpection(map);
        /** 删除规格选项列表 */
        String spectionId = (String) map.get("spectionId");
        logger.info("spectionId{}:" + JSON.toJSONString(spectionId));
        optionDao.deleteOptionBySpectionId(spectionId);
        /** 添加规格选项列表 */
        Spection spection = JSON.parseObject(JSON.toJSONString(map)).toJavaObject(Spection.class);
        // List<Option> optionList = (List<Option>) map.get("optionList");
        /** 特别注意前端传过来的不能为Spection对象,应为没有对应的optionList这个属性 */
        List<Option> optionList = JSON.parseObject(JSON.toJSONString(map)).getJSONArray("optionList").toJavaList(Option.class);
        setOptionIdAndSpectionId(spection, optionList);
        optionDao.addOptionList(optionList);
    }

    @Override
    public List<Map<String, Object>> selectAllSpection() {
        /**
         * @Description: 查询所有规格列表
         * @methodName: selectAllSpection
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Spection>
         * @Author: scyang
         * @Date: 2020/2/6 21:39
         */
        List<Map<String, Object>> mapList = spectionDao.selectAllSpection();
        logger.info("mapList{}:" + JSON.toJSONString(mapList));
        if (!CollectionsUtils.isListEmpty(mapList)) {
            for (Map<String, Object> map : mapList) {
                /** 根据spectionId获取规格选项列表,从数据库里获得的字段 */
                String spection_id = (String) map.get("spection_id");
                logger.info("spection_id{}:" + JSON.toJSONString(spection_id));
                List<Map<String, Object>> optionList = optionDao.selectOptionListBySpectionId(spection_id);
                logger.info("optionList{}:" + JSON.toJSONString(optionList));
                if (!CollectionsUtils.isListEmpty(optionList)) {
                    List<Map<String, Object>> option_status_one = optionList.stream().filter(optionMap -> OPTION_STATUS_ONE.equals(optionMap.get("option_status"))).collect(Collectors.toList());
                    logger.info("option_status_one{}:" + JSON.toJSONString(option_status_one));
                    List<Map<String, Object>> option_status_two = optionList.stream().filter(optionMap -> OPTION_STATUS_TWO.equals(optionMap.get("option_status"))).collect(Collectors.toList());
                    logger.info("option_status_two{}:" + JSON.toJSONString(option_status_two));
                    /*Map<String, Object> status_map = new HashMap<>();
                    status_map.put("option_status_one", option_status_one);
                    status_map.put("option_status_two", option_status_two);*/
                    map.put("option_status_one", option_status_one);
                    map.put("option_status_two", option_status_two);
                    logger.info("map{}:" + JSON.toJSONString(map));
                }
            }
        }
        logger.info("mapList{}:" + JSON.toJSONString(mapList));
        logger.info("spectionList{}:" + JSON.toJSONString(JSON.parseArray(JSON.toJSONString(mapList), Spection.class)));
        return mapList;
    }

    @Override
    public List<Spection> findAllSpection() {
        /**
         * @Description: 查询所有规格列表
         * @methodName: findAllSpection
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Spection>
         * @Author: scyang
         * @Date: 2020/2/6 23:57
         */
        /** 首先从redis中查找 */
        List<Spection> spectionList = (List<Spection>) redisTemplate.opsForValue().get("spectionList");
        if (CollectionsUtils.isListEmpty(spectionList)) {
            spectionList = spectionDao.findAllSpection();
            //Map<String,List<Option>> option_status_map=new HashMap<>();
            logger.info("spectionList{}:" + JSON.toJSONString(spectionList));
           /* for (Spection spection : spectionList) {
                Map<String,List<Option>> option_status_map=new HashMap<>();
                String spectionId = spection.getSpectionId();
                List<Option> optionList=optionDao.findSpectionListById(spectionId);
                List<Option> option_status_one = optionList.stream().filter(option -> OPTION_STATUS_ONE.equals(option.getOptionStatus())).collect(Collectors.toList());
                List<Option> option_status_two = optionList.stream().filter(option -> OPTION_STATUS_TWO.equals(option.getOptionStatus())).collect(Collectors.toList());
                option_status_map.put("option_status_one",option_status_one );
                option_status_map.put("option_status_two",option_status_two );
                spection.setOptionMap(option_status_map);
                logger.info("spection{}:"+JSON.toJSONString(spection));
                redisTemplate.opsForValue().set("spectionList",spectionList , 5, TimeUnit.MINUTES );
            }*/
            Map<String, Spection> spectionMap = spectionList.stream().collect(Collectors.toMap(Spection::getSpectionId, spection -> spection));
            logger.info("spectionMap{}:" + JSON.toJSONString(spectionMap));
            for (Map.Entry<String, Spection> spectionEntry : spectionMap.entrySet()) {
                String spectionId = spectionEntry.getKey();
                Spection spection = spectionEntry.getValue();
                Map<String, List<Option>> option_status_map = new HashMap<>();
                List<Option> optionList = optionDao.findSpectionListById(spectionId);
                List<Option> option_status_one = optionList.stream().filter(option -> OPTION_STATUS_ONE.equals(option.getOptionStatus())).collect(Collectors.toList());
                List<Option> option_status_two = optionList.stream().filter(option -> OPTION_STATUS_TWO.equals(option.getOptionStatus())).collect(Collectors.toList());
                option_status_map.put("option_status_one", option_status_one);
                option_status_map.put("option_status_two", option_status_two);
                spection.setOptionMap(option_status_map);
                logger.info("spection{}:" + JSON.toJSONString(spection));
                redisTemplate.opsForValue().set("spectionList", spectionList, 5, TimeUnit.MINUTES);
            }
        } else {
            logger.info("从redis中查找 spectionList{}:" + JSON.toJSONString(spectionList));
        }
        return spectionList;
    }

    @Override
    public void addOption(String paramJson) {
        /**
         * @Description: 添加规格选项表
         * @methodName: addOption
         * @Param: [option]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/7 1:06
         */
        Map<String, Object> map = JSON.parseObject(paramJson).toJavaObject(Map.class);
        map.put("optionId", idWorker.nextId() + "");
        map.put("spectionId", idWorker.nextId() + "");
        //String spectionId = (String) map.get("spectionId");
        optionDao.addOption(map);
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map));
        Spection spection = jsonObject.getJSONObject("spection").toJavaObject(Spection.class);
        spection.setSpectionId((String) map.get("spectionId"));
        spectionDao.addSpection(spection);
        String s = "[{\"黄鹤楼\":\"武昌区\"},{\"归元寺\":\"洪山区\"}]";
    }

    @Override
    public void deleteOptionByIds(String[] ids) {
        /**
         * @Description: 批量删除规格选项列表
         * @methodName: deleteOptionByIds
         * @Param: []
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/7 3:37
         */
        spectionDao.delteSpectionByIds(ids);
        optionDao.deleteOptionBySpectionIds(ids);
    }

    @Override
    public void updateOptionBySpectionId(String paramJson) {
        /**
         * @Description: 修改规格选项列表
         * @methodName: updateOptionBySpectionId
         * @Param: [paramJson]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/7 4:18
         */
        Map<String, Object> map = JSON.parseObject(paramJson).toJavaObject(Map.class);
        Option option = JSON.parseObject(JSON.toJSONString(map)).toJavaObject(Option.class);
        option.setOptionId(idWorker.nextId() + "");
        option.setSpetionId(idWorker.nextId() + "");
        logger.info("option{}:" + JSON.toJSONString(option));
        /** 更新规格选项列表 */
        optionDao.updateOption(option);
        /** 根据spectionId删除规格列表 */
        spectionDao.deleteSpectionbyId(option.getSpetionId());
        /** 新增规格列表 */
        Spection spection = JSON.parseObject(JSON.toJSONString(map)).getJSONObject("spection").toJavaObject(Spection.class);
        spection.setSpectionId(option.getSpetionId());
        logger.info("spection{}:" + JSON.toJSONString(spection));
        spectionDao.addSpection(spection);
    }

    @Override
    public Map<String, Object> selectAll() {
      /*  List<Map<String, Object>> mapList = spectionDao.selectAllSpection();
        List<Option>optionList= optionDao.selectAllOption();
        Map<String,Object> map=new HashMap<>();
        map.put("mapList", mapList);
        map.put("optionList", optionList);*/

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            logger.info("i{}:" + i);
            new Runnable() {
                @Override
                public void run() {
                    synchronized (SpectionServinceImpl.class) {
                        List<Map<String, Object>> mapList = spectionDao.selectAllSpection();
                        List<Option> optionList = optionDao.selectAllOption();
                        // Map<String, Object> map = new HashMap<>();
                        map.put("mapList", mapList);
                        map.put("optionList", optionList);
                    }
                }
            }.run();
        }
        return map;
    }

    @Override
    public PageInfo<Option> pageInfo(Integer pageNum, Integer pageSize) {
        /**
         * @Description: 分页查询
         * @methodName: pageInfo
         * @Param: [pageNum, pageSize]
         * @return: com.github.pagehelper.PageInfo<com.tensquare.article.pojo.Option>
         * @Author: scyang
         * @Date: 2020/2/7 6:33
         */
        PageHelper.startPage(pageNum, pageSize);
        List<Option> optionList = optionDao.selectAllOption();
        //jmsTemplate.convertAndSend("option", optionList);
        jmsTemplate.send("option", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                String toJSONString = JSON.toJSONString(optionList);
                TextMessage textMessage = session.createTextMessage(toJSONString);
                return textMessage;
            }
        });
        PageInfo<Option> pageInfo = new PageInfo<>(optionList);
        return pageInfo;
    }

    @Override
    public PageBean<Option> pageBean(Integer pageNum, Integer pageSize) throws ExecutionException, InterruptedException {
        /**
         * @Description: 分页查询
         * @methodName: pageBean
         * @Param: [pageNum, pageSize]
         * @return: common.PageBean<com.tensquare.article.pojo.Option>
         * @Author: scyang
         * @Date: 2020/2/7 6:43
         */
       /* SpectionRunnable spectionRunnable = new SpectionRunnable(  pageNum, pageSize);
        FutureTask futureTask = new FutureTask(spectionRunnable);
        new Thread(futureTask).start();
        return (PageBean<Option>) futureTask.get();*/
       /* try {
            PageBean<Option> pageBean =new PageBean<>();
            ThreadUtils.getThread(new Runnable() {
                @Override
                public void run() {
                    logger.info("线程的名字: "+Thread.currentThread().getName());
                    Integer tatolCount = optionDao.getTatolCount();
                    Integer index = (pageNum - 1) * pageSize;
                    List<Option> optionList = optionDao.selectPage(index, pageSize);
                    //Integer totalPage=(int) Math.ceil(tatolCount*1.0/pageSize);
                    Integer totalPage = tatolCount % pageSize == 0 ? tatolCount % pageSize : tatolCount / pageSize + 1;
                    pageBean.setPageNum(pageNum);
                    pageBean.setPageSize(pageSize);
                    pageBean.setTatolCount(tatolCount);
                    pageBean.setTotalPage(totalPage);
                    pageBean.setData(optionList);
                }
            });
            return pageBean;
        } catch (Exception e) {
            throw new RuntimeException("分页查询失败" + e.getMessage());
        }*/
        FutureTask<Object> futureTask = null;
        for (int i = 0; i < 2; i++) {
            futureTask = new FutureTask<Object>(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    synchronized (SpectionServinceImpl.class) {
                        logger.info("线程的名字: " + Thread.currentThread().getName());
                        PageBean<Option> pageBean = new PageBean<>();
                        Integer tatolCount = optionDao.getTatolCount();
                        Integer index = (pageNum - 1) * pageSize;
                        List<Option> optionList = optionDao.selectPage(index, pageSize);
                        //Integer totalPage=(int) Math.ceil(tatolCount*1.0/pageSize);
                        Integer totalPage = tatolCount % pageSize == 0 ? tatolCount % pageSize : tatolCount / pageSize + 1;
                        pageBean.setPageNum(pageNum);
                        pageBean.setPageSize(pageSize);
                        pageBean.setTatolCount(tatolCount);
                        pageBean.setTotalPage(totalPage);
                        pageBean.setData(optionList);
                        return pageBean;
                    }
                }
            });
            new Thread(futureTask).start();
        }
        return (PageBean<Option>) futureTask.get();
    }

    @Override
    public Set<String> getListString() {
        List<Option> optionList = optionDao.selectAllOption();
        List<String> stringList = getString(optionList, 5);
        Set<String> stringSet = new HashSet<>(stringList);
        return stringSet;
    }

    @Override
    public List<Option> getOptionList(Date spectionDate) {
        /**
         * @Description: 根据规格时间查询规格选项列表
         * @methodName: getOptionList
         * @Param: [spectionDate]
         * @return: java.util.List<com.tensquare.article.pojo.Option>
         * @Author: scyang
         * @Date: 2020/2/10 0:49
         */
        List<Spection> spectionList = spectionDao.getOptionList(spectionDate);
        logger.info("spectionList{}:" + JSON.toJSONString(spectionList));
        List<Option> optionList = null;
        if (!CollectionsUtils.isListEmpty(spectionList)) {
            List<String> spection_code_list = spectionList.stream().
                    filter(spection -> spection.getSpectionCode()
                            .length() == 1).map(spection -> spection.getSpectionCode())
                    .collect(Collectors.toList());
            logger.info("spection_code_list{}:" + JSON.toJSONString(spection_code_list));
            optionList = getOptionListByStatus(spection_code_list);
            List<Option> finalOptionList = optionList;
            jmsTemplate.send("option_status", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    String textMsg = JSON.toJSONString(finalOptionList);
                    return session.createTextMessage(textMsg);
                }
            });
        }
        return optionList;
    }

    @Override
    public void addDepartmentList(Department[] departmentArray) {
        /**
         * @Description: 批量添加机构列表
         * @methodName: addDepartmentList
         * @Param: [departmentArray]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/11 23:15
         */
        /** 根据机构代码设置机构名称 */
        setDepartmentName(departmentArray);
        logger.info("departmentArray{service层}:" + JSON.toJSONString(departmentArray));
        departmentDao.addDepartmentList(departmentArray);
    }

    @Override
    public List<Department> getDepartmentList() {
        /**
         * @Description: 查询所有的机构列表
         * @methodName: getDepartmentList
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Department>
         * @Author: scyang
         * @Date: 2020/2/12 13:56
         */
        List<Department> departmentList = departmentDao.getDepartmentList();
        departmentList = getReturnList(departmentList);
        logger.info("departmentList{service层}:" + JSON.toJSONString(departmentList));
        return departmentList;
    }

    @Override
    public List<Department> getDepartmentListByCode(String departmentCode) {
        /**
         * @Description: 根据机构代码查询机构列表
         * @methodName: getDepartmentListByCode
         * @Param: [department]
         * @return: java.util.List<com.tensquare.article.pojo.Department>
         * @Author: scyang
         * @Date: 2020/2/12 21:59
         */
        /** 开关控制 */
        List<Department> returnList = null;
        boolean switchValue = SwitchFetchUtils.getBooleanSwitchValue("", false);
        if (switchValue) {
            return returnList;
        }
        if (Constant.DEPARTMENT_LEVEL_FOUR.equals(departmentCode)) {
            returnList = getDepartmentListByFour(departmentCode);
        } else {
            returnList = getDepartmentListByOhter(departmentCode);
        }
        return returnList;
    }

    @Override
    public void addPolicyList(List<Policy> policyList) {
        /**
         * @Description: 批量添加保单对象
         * @methodName: addPolicyList
         * @Param: [policyList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/13 21:32
         */
        if (!CollectionsUtils.isListEmpty(policyList)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(1949 - 1900, 9 - 1, 1, 15, 25, 27));
            for (Policy policy : policyList) {
                /** 设置主键 */
                policy.setPolicyId(idWorker.nextId() + "");
                /** 设置保单号 */
                policy.setPolicyNo(11568 + "" + numUtils.getNum(6));
                String accidentReson = getAccidentReason(policy.getInsuredName());
                /** 设置出险原因 */
                policy.setAccidentReason(accidentReson);
                /** 设置出险时间 */
                calendar.add(Calendar.YEAR, -1);
                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DAY_OF_MONTH, 3);
                policy.setAccidentDate(calendar.getTime());
                /** 设置案件状态值  */
                setCaseValue(policy);
                /** 设置币种描叙 */
                setCurreenctDesc(policy);
                /** 设置赔付金额 */
                policy.setPayAmount(policy.getSettlendAmount().add(policy.getOutstandAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                BigDecimal settlendAmount = policy.getSettlendAmount();
                BigDecimal outstandAmount = policy.getOutstandAmount();
                DecimalFormat df = new DecimalFormat("0.00%");
                BigDecimal settlendRate = settlendAmount.divide(policy.getPayAmount(), 4, BigDecimal.ROUND_UP);
                /** 设置已决金额率 */
                policy.setSettlendRate(df.format(settlendRate));
                BigDecimal outstandRate = outstandAmount.divide(policy.getPayAmount(), 4, BigDecimal.ROUND_UP);
                /** 设置未决金额率 */
                policy.setOutstandRate(df.format(outstandRate));
            }
        }
        logger.info("policyList{}:" + JSON.toJSONString(policyList));
        policyDao.addPolicyList(policyList);
    }

    @Override
    public List<Policy> selectPoicyList() {
        /**
         * @Description: 查询所有的保单对象, 被保险的名称相同的合并, 已决和未决金额相加
         * @methodName: selectPoicyList
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Policy>
         * @Author: scyang
         * @Date: 2020/2/14 13:15
         */
        List<Policy> policyList = policyDao.selectPoicyList();
        logger.info("policyList{service}:" + JSON.toJSONString(policyList));
        for (int i = policyList.size() - 1; i >= 0; i--) {
            Policy policyI = policyList.get(i);
            BigDecimal settlendAmount = policyI.getSettlendAmount();
            BigDecimal outstandAmount = policyI.getOutstandAmount();
            for (int j = 0; j < i; j++) {
                Policy policyJ = policyList.get(j);
                if (policyI.getInsuredName().equals(policyJ.getInsuredName())) {
                    settlendAmount = settlendAmount.add(policyJ.getSettlendAmount()).setScale(2, BigDecimal.ROUND_UP);
                    outstandAmount = outstandAmount.add(policyJ.getOutstandAmount()).setScale(2, BigDecimal.ROUND_UP);
                    policyJ.setSettlendAmount(settlendAmount);
                    policyJ.setOutstandAmount(outstandAmount);
                    policyJ.setPayAmount(settlendAmount.add(outstandAmount).setScale(2, BigDecimal.ROUND_UP));
                    DecimalFormat df = new DecimalFormat("0.00%");
                    policyJ.setSettlendRate(df.format(settlendAmount.divide(policyJ.getPayAmount(), 4, BigDecimal.ROUND_UP)));
                    policyJ.setOutstandRate(df.format(outstandAmount.divide(policyJ.getPayAmount(), 4, BigDecimal.ROUND_UP)));
                    policyDao.updatePolicy(policyJ);
                    policyDao.deletePolicyById(policyI);
                    policyList.remove(i);
                    break;
                }
            }
        }
        logger.info("policyList的长度：" + policyList.size());
        return policyList;
    }

    @Override
    public Map<String, Object> getSettlendAndOutstand() {
        /**
         * @Description: 查询保单的已决金额未决金额的总和
         * @methodName: getSettlendAndOutstand
         * @Param: []
         * @return: java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               j                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               a                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               v                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               a                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               .                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               l                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               a                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               g.Object>
         * @Author: scyang
         * @Date: 2020/2/14 22:19
         */
        List<Policy> policyList = policyDao.selectPoicyList();

        Map<String, Object> returnMap = new HashMap<>();
        BigDecimal settlendAmountSum = BigDecimal.ZERO;
        BigDecimal outstandAmountSum = BigDecimal.ZERO;
        BigDecimal payAmountSum = BigDecimal.ZERO;
        if (!CollectionsUtils.isListEmpty(policyList)) {
            for (Policy policy : policyList) {
                if (Constant.CURRENCY_CODE_CNY.equals(policy.getCurrencyCode())) {
                    settlendAmountSum = settlendAmountSum.add(policy.getSettlendAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    outstandAmountSum = outstandAmountSum.add(policy.getOutstandAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    payAmountSum = payAmountSum.add(policy.getPayAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                } else if (Constant.CURRENCY_CODE_HKD.equals(policy.getCurrencyCode())) {
                    settlendAmountSum = settlendAmountSum.add(policy.getSettlendAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    outstandAmountSum = outstandAmountSum.add(policy.getOutstandAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    payAmountSum = payAmountSum.add(policy.getPayAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                } else if (Constant.CURRENCY_CODE_AUD.equals(policy.getCurrencyCode())) {
                    settlendAmountSum = settlendAmountSum.add(policy.getSettlendAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    outstandAmountSum = outstandAmountSum.add(policy.getOutstandAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    payAmountSum = payAmountSum.add(policy.getPayAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                } else if (Constant.CURRENCY_CODE_TWD.equals(policy.getCurrencyCode())) {
                    settlendAmountSum = settlendAmountSum.add(policy.getSettlendAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    outstandAmountSum = outstandAmountSum.add(policy.getOutstandAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                    payAmountSum = payAmountSum.add(policy.getPayAmount().multiply(policy.getExchangeRate())).setScale(2, BigDecimal.ROUND_UP);
                }
            }
        }
        returnMap.put("settlendAmountSum", settlendAmountSum);
        returnMap.put("outstandAmountSum", outstandAmountSum);
        returnMap.put("payAmountSum", payAmountSum);
        return returnMap;
    }

    @Override
    public void addGuaranteeList(List<Guarantee> guaranteeList) {
        /**
         * @Description: 批量添加担保函信息表
         * @methodName: addGuaranteeList
         * @Param: []
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/15 20:25
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2020 - 1900, 1 - 1, 15, 16, 27, 56));
        for (Guarantee guarantee : guaranteeList) {
            /** 主键 */
            guarantee.setGuaranteeId(idWorker.nextId() + "");
            /** 报案号 */
            guarantee.setPolicyNo(11562 + "" + numUtils.getNum(6));
            calendar.add(Calendar.DAY_OF_MONTH, -10);
            calendar.add(Calendar.HOUR_OF_DAY, 5);
            calendar.add(Calendar.MINUTE, 10);
            calendar.add(Calendar.SECOND, 6);
            /**事故发生时间  */
            guarantee.setAccidentDate(calendar.getTime());
            /** 设置字符串日期 */
            guarantee.setAccidentDateStr(DateUtil.DateToStr(guarantee.getAccidentDate(), DateUtil.FORMATTWO));
            /** 设置保单序列号 */
            String dateToStr = DateUtil.DateToStr(guarantee.getAccidentDate(), DateUtil.FORMATFIVE);
            String leftPad = StringUtils.leftPad(guarantee.getInsuredName(), 16, "#");
            guarantee.setGuaranteeSeriaNo(dateToStr + leftPad);
            /** 构建担保函扩张表 */
            List<GuaranteeExt> guaranteeExtList = GuaranteeUtils.buillGuaranteeExtList(guarantee);
            guaranteeExtDao.addGuaranteeExtList(guaranteeExtList);
        }
        guaranteeDao.addGuaranteeList(guaranteeList);
    }

    @Scheduled(cron = "0 15 10 * * ? ")
    @Override
    public List<Guarantee> selectGuaranteeList() {
        /**
         * @Description: 查询所有的担保函信息
         * @methodName: selectGuaranteeList
         * @Param: []
         * @return: java.util.List<entity.Guarantee>
         * @Author: scyang
         * @Date: 2020/2/15 23:00
         */
        List<Guarantee> guaranteeList = guaranteeDao.selectGuaranteeList();
        for (Guarantee guarantee : guaranteeList) {
            List<GuaranteeExt> guaranteeExtList = guaranteeExtDao.selectGuaranteeExtList(guarantee.getGuaranteeId());
            GuaranteeExt guaranteeExt = GuaranteeUtils.buillGuaranteeExt(guaranteeExtList, guarantee.getProductClass());
            logger.info("guaranteeExt{service层}:" + JSON.toJSONString(guaranteeExt));
            guarantee.setGuaranteeExt(guaranteeExt);
        }
        return guaranteeList;
    }

    @Override
    public void addInsuredList(String paramJson) throws Exception {
        /**
         * @Description: 批量添加保险人信息
         * @methodName: addInsuredList
         * @Param: [paramJson]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/16 20:43
         */
        List<Map> javaList = JSON.parseObject(paramJson).getJSONArray("insuredList").toJavaList(Map.class);
        Map<String, Object> paramMap = null;
        for (Map map : javaList) {
            String idCard = (String) map.get("idCard");
            String insuredDesc = (String) map.get("insuredDesc");
            String insuredName = (String) map.get("insuredName");
            String conclusionCode = (String) map.get("conclusionCode");
            paramMap = new HashMap<>();
            paramMap.put("insuredId", idWorker.nextId() + "");
            paramMap.put("policyNo", 11568 + "" + numUtils.getNum(6));
            paramMap.put("idCard", idCard);
            //paramMap.put("conclusionCode", conclusionCode);
            paramMap.put("insuredName", insuredName);
            paramMap.put("sex", StringUtils.getGenderFromIdCard(idCard));
            paramMap.put("birthday", StringUtils.getBirthdayFromIdCard(idCard));
            paramMap.put("age", StringUtils.getAgeFromIdCard(idCard));
            String jsonString = JSON.toJSONString(map);
            jsonString = getJsonString(jsonString);
            String StringJson = getStringJson(conclusionCode, insuredDesc);
            Map map2 = JSON.parseObject(StringJson, Map.class);
            Map map1 = JSON.parseObject(jsonString).toJavaObject(Map.class);
            paramMap.put("map1", map1);
            paramMap.put("map2", map2);
            insuredDao.addInsured(paramMap);
        }
    }

    @Override
    public List<Insured> selectInsuredList() {
        /**
         * @Description: 批量查询被保险人信息
         * @methodName: selectInsuredList
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Insured>
         * @Author: scyang
         * @Date: 2020/2/17 0:10
         */
        List<Insured> insuredList = insuredDao.selectInsuredList();
        String weight2 = JSON.parseArray(JSON.toJSONString(insuredList)).getJSONObject(2).getJSONArray("insuredDesc").getJSONObject(1).getString("weight");
        logger.info("weight2{}:" + weight2);
        String insuredName = JSON.parseArray(JSON.toJSONString(insuredList)).getJSONObject(2).getObject("insuredName", String.class);
        logger.info("insuredName{}:" + insuredName);

        if (!CollectionsUtils.isListEmpty(insuredList)) {
          /* List<String> descList = insuredList.stream().map(Insured::getInsuredDesc).collect(Collectors.toList());
           for (String insuredDesc : descList) {
              // String height = JSON.parseObject(insuredDesc).getString("height");错误
               //String weight = JSON.parseObject(insuredDesc).getString("weight");错误
               String height = JSON.parseArray(insuredDesc).getJSONObject(0).getString("height");
               String weight = JSON.parseArray(insuredDesc).getJSONObject(1).getString("weight");
               setInsuredMap(insuredList,height,weight);
           }*/
            for (Insured insured : insuredList) {
                /** insured转了,不能再用get方法 */
                String insuredName1 = JSON.parseObject(JSON.toJSONString(insured)).getObject("insuredName", String.class);
                logger.info("insuredName1{}:" + insuredName1);
                String height1 = JSON.parseObject(JSON.toJSONString(insured)).getJSONArray("insuredDesc").getJSONObject(0).getObject("height", String.class);
                logger.info("height1{}:" + height1);

                String height = JSON.parseArray(insured.getInsuredDesc()).getJSONObject(0).getString("height");
                String weight = JSON.parseArray(insured.getInsuredDesc()).getJSONObject(1).getString("weight");
                Map<String, Object> map = new HashMap<>();
                map.put("height", height);
                map.put("weight", weight);
                insured.setMap(map);
            }
        }
        return insuredList;
    }

    @Override
    public void addRiskBackList(List<RiskBack> riskBackList) {
        /** 批量添加风险反馈信息表 */
        for (RiskBack riskBack : riskBackList) {
            /** 添加主键 */
            riskBack.setRiskBackId(idWorker.nextId() + "");
            /** 添加报案号 */
            riskBack.setReportNo(11568 + "" + numUtils.getNum(6));
            /** 设置保险人身份证号 */
            if (StringUtils.isIdCard(riskBack.getInsuredIdCard())) {
                riskBack.setInsuredIdCard(StringUtils.middleSuff(riskBack.getInsuredIdCard(), 6, 4));
            }
            /** 设置运输车牌号 */
            if (StringUtils.isCarCard(riskBack.getPlateNumber())) {
                riskBack.setPlateNumber(riskBack.getPlateNumber() + "*挂");
            }
            /** 设置手机号 */
            riskBack.setRiskDriveTel(StringUtils.midlleMask(riskBack.getRiskDriveTel(), 3, 4));
            /** 设置反馈结果值 */
            setRiskBackValue(riskBack);

        }
        /** 批量添加风险反馈信息 */
        riskBackDao.addRiskBackList(riskBackList);
    }

    @Override
    public void updateRiskBackList(String riskBackCode) {
        /**
         * @Description: 批量修改风险反馈信息
         * @methodName: updateRiskBackList
         * @Param: [riskBackCode]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/19 11:02
         */

        /** 若证件号、报案手机号、车牌号连续两次反馈的结果值为“无风险”则将该证件号、报案手机号
         车牌号移出风险名单2 */
        /** 若证件号、报案手机号、车牌号连续三次反馈的结果值为“虚报风险”则将该证件号、报案手机号
         车牌号加入风险名单1 */
        /** 反馈为“保险欺诈”或“其他风险”加入风险名单4 */

        String times_2 = "";/** 连续出险两次 */
        String times_3 = "";/** 连续出险三次 */
        String fxmd1_sxx = "";/** 风险名单1,默认时效性1年 */
        String fxmd4_sxx = "";/** 风险名单4,默认时效性4年 */
        List<ExtendInfo> extendInfoList = extendDao.selectAll();
        logger.info("extendInfoList{}:" + JSON.toJSONString(extendInfoList));

        if (!CollectionsUtils.isListEmpty(extendInfoList)) {
            for (ExtendInfo extendInfo : extendInfoList) {
                if ("times_2".equals(extendInfo.getExtendCode())) {
                    times_2 = extendInfo.getExtendValue();
                }
                if ("times_3".equals(extendInfo.getExtendCode())) {
                    times_3 = extendInfo.getExtendValue();
                }
                if ("fxmd1_sxx".equals(extendInfo.getExtendCode())) {
                    fxmd1_sxx = extendInfo.getExtendValue();
                }
                if ("fxmd4_sxx".equals(extendInfo.getExtendCode())) {
                    fxmd4_sxx = extendInfo.getExtendValue();
                }
                if (!"".equals(times_2) && !"".equals(times_3) && !"".equals(fxmd1_sxx) && !"".equals(fxmd4_sxx)) {
                    break;
                }
            }
            /** 反馈类型为 无风险-0 连续2次 */
            if (Constant.WITHOUT_RISK.equals(riskBackCode)) {
                /** 根据反馈类型值查出证件号集合 */
                List<String> insuredIdCardList = getIdCardList(riskBackCode);
                logger.info("idCardList{}:" + JSON.toJSONString(insuredIdCardList));
                /** 根据反馈类型值查出报案手机号集合 */
                List<String> driveTelList = getDriveTelList(riskBackCode);
                logger.info("driveTelList{}:" + JSON.toJSONString(driveTelList));
                /** 根据反馈类型值查出运输车牌号集合 */
                List<String> plateNumberList = getPlateNumberList(riskBackCode);
                logger.info("plateNumberList{}:" + JSON.toJSONString(plateNumberList));
                /** 根据保险人身份证号查询反馈的结果是否连续两次为无风险 */
                Map<String, Object> map = new HashMap();
                map.put("insuredIdCardList", insuredIdCardList);
                map.put("times_2", times_2);
                // map.put("driveTelList", driveTelList);
                //map.put("plateNumberList", plateNumberList);
                Map<String, Object> paramMap = getParamMap2(map, riskBackCode);
                logger.info("paramMap{}:" + JSON.toJSONString(paramMap));
                boolean isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                List<RiskBack> paramList = (List<RiskBack>) paramMap.get("paramList");
                logger.info("paramList{}paramList的长度为:" + paramList.size());
                /** 根据报案电话查询反馈的结果是否连续两次为无风险 */
                if (!isWithOutRisk) {
                    map.remove("insuredIdCardList");
                    map.put("driveTelList", driveTelList);
                    paramMap = getParamMap2(map, riskBackCode);
                    isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                    if (!isWithOutRisk) {
                        map.remove("driveTelList");
                        map.put("plateNumberList", plateNumberList);
                        paramMap = getParamMap2(map, riskBackCode);
                        isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                    }
                }
                if (isWithOutRisk) {
                    /** 批量修改,移除风险名单1 */
                    updateRiskBacklist(paramList, "移除风险名单1", "无效");
                }
            }
            /** 反馈类型为 虚报风险-1 连续3次 */
            if (Constant.FALSE_LOSS.equals(riskBackCode)) {
                /** 根据反馈类型值查出证件号集合 */
                List<String> insuredIdCardList = getIdCardList(riskBackCode);
                logger.info("idCardList{}:" + JSON.toJSONString(insuredIdCardList));
                /** 根据反馈类型值查出报案手机号集合 */
                List<String> driveTelList = getDriveTelList(riskBackCode);
                logger.info("driveTelList{}:" + JSON.toJSONString(driveTelList));
                /** 根据反馈类型值查出运输车牌号集合 */
                List<String> plateNumberList = getPlateNumberList(riskBackCode);
                logger.info("plateNumberList{}:" + JSON.toJSONString(plateNumberList));
                /** 根据身份证号查询反馈的结果是否连续三次虚报风险 */
                Map<String, Object> map = new HashMap();
                map.put("insuredIdCardList", insuredIdCardList);
                map.put("times_3", times_3);
                // map.put("driveTelList", driveTelList);
                //map.put("plateNumberList", plateNumberList);
                Map<String, Object> paramMap = getParamMap3(map, riskBackCode);
                logger.info("paramMap{}:" + JSON.toJSONString(paramMap));
                boolean isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                List<RiskBack> paramList = (List<RiskBack>) paramMap.get("paramList");
                logger.info("paramList{}paramList的长度为:" + paramList.size());
                /** 根据报案电话查询反馈的结果是否连续三次虚报风险 */
                if (!isWithOutRisk) {
                    map.remove("insuredIdCardList");
                    map.put("driveTelList", driveTelList);
                    paramMap = getParamMap3(map, riskBackCode);
                    isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                    if (!isWithOutRisk) {
                        map.remove("driveTelList");
                        map.put("plateNumberList", plateNumberList);
                        paramMap = getParamMap3(map, riskBackCode);
                        isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                    }
                }
                if (isWithOutRisk) {
                    /** 批量修改,移加入风险名单2 */
                    updateRiskBacklist(paramList, "加入风险名单2", "有效");
                }
            }
            /** 反馈类型为 保险欺诈或其他风险-2-3 连续3次 */
            if (Constant.INSURED_CHEAT.equals(riskBackCode) || Constant.OTHER_RISK.equals(riskBackCode)) {
                /** 根据反馈类型值查出证件号集合 */
                List<String> insuredIdCardList = getIdCardList(riskBackCode);
                logger.info("idCardList{}:" + JSON.toJSONString(insuredIdCardList));
                /** 根据反馈类型值查出报案手机号集合 */
                List<String> driveTelList = getDriveTelList(riskBackCode);
                logger.info("driveTelList{}:" + JSON.toJSONString(driveTelList));
                /** 根据反馈类型值查出运输车牌号集合 */
                List<String> plateNumberList = getPlateNumberList(riskBackCode);
                logger.info("plateNumberList{}:" + JSON.toJSONString(plateNumberList));
                /** 根据保险人身份证号查询反馈的结果是否连续三次为保险欺诈或其他风险为无风险 */
                Map<String, Object> map = new HashMap();
                map.put("insuredIdCardList", insuredIdCardList);
                map.put("times_3", times_3);
                // map.put("driveTelList", driveTelList);
                //map.put("plateNumberList", plateNumberList);
                Map<String, Object> paramMap = getParamMap3(map, riskBackCode);
                logger.info("paramMap{}:" + JSON.toJSONString(paramMap));
                boolean isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                List<RiskBack> paramList = (List<RiskBack>) paramMap.get("paramList");
                logger.info("paramList{}paramList的长度为:" + paramList.size());
                /** 根据报案电话查询反馈的结果是否连续三次为保险欺诈或其他风险为无风险 */
                if (!isWithOutRisk) {
                    map.remove("insuredIdCardList");
                    map.put("driveTelList", driveTelList);
                    paramMap = getParamMap3(map, riskBackCode);
                    isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                    if (!isWithOutRisk) {
                        map.remove("driveTelList");
                        map.put("plateNumberList", plateNumberList);
                        paramMap = getParamMap3(map, riskBackCode);
                        isWithOutRisk = (boolean) paramMap.get("isWithOutRisk");
                    }
                }
                if (isWithOutRisk) {
                    /** 批量修改,加入风险名单4 */
                    updateRiskBacklist(paramList, "加入风险名单4", "有效");
                }
            }
        }
        logger.info("times_ 2 {}:" + times_2);
        logger.info("times_ 3 {}:" + times_3);
        logger.info("fxmd1_sx x {}:" + fxmd1_sxx);
        logger.info("fxmd4_sx x {}:" + fxmd4_sxx);
    }

    @Override
    public void addSalvageList(List<Map> mapList) {
        /**
         * @Description: 批量添加残值信息
         * @methodName: addSalvageList
         * @Param: [mapList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/23 3:18
         */

        if (!CollectionsUtils.isListEmpty(mapList)) {
            int count = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(2020 - 1900, 2 - 1, 23, 03, 25, 16));
            BigDecimal damageDate = BigDecimal.ZERO;
            for (Map map : mapList) {
                /** 添加主键 */
                map.put("salvageId", idWorker.nextId() + "");
                /** 添加序列号 */
                map.put("seriaNo", getSeriaNo(calendar));
                /** 添加残值名称 */
                String salvageName = getSalvageName(count);
                map.put("salvageName", salvageName);
                count++;
                // String salvageName = (String) map.get("salvageName");
                /** 添加状态值 */
                map.put("statusValue", getStatusValue((String) map.get("statusCode")));
                /** 添加受损时间 */
                map.put("damageStartDate", getDamageStatrDate(calendar));
                /** 设置受损天数 */
                damageDate = damageDate.add(new BigDecimal("2"));
                logger.info("damageDate{}:" + damageDate);
                map.put("damageDay", damageDate);
                /** 添加受损截止日期 */
                map.put("damageEndDate", getDamageEndDate(getDamageStatrDate(calendar), damageDate));
                /** 添加残值描叙 */
                map.put("salvageDesc", getSalvageDesc(salvageName));
                /** 添加残值网址 */
                map.put("salvageWeb", getSalvageWeb(salvageName));
            }
            /** 批量添加残值信息 */
            salvageDao.addSalvageList(mapList);
        }
    }

    @Override
    public PageInfo<Salvage> getSalvagePageInfo(Integer pageNumber, Integer pageSize, String statusCode) {
        /**
         * @Description: 分页查询残值信息
         * @methodName: getSalvagePageInfo
         * @Param: [pageNumber, pageSize, statusCode]
         * @return: com.github.pagehelper.PageInfo<com.tensquare.article.pojo.Salvage>
         * @Author: scyang
         * @Date: 2020/2/23 17:33
         */
        PageHelper.startPage(pageNumber, pageSize);
        List<Salvage> salvageList = salvageDao.getSalvagePageInfo(statusCode);
        logger.info("salvageList{}:" + JSON.toJSONString(salvageList));
        PageInfo<Salvage> salvagePageInfo = new PageInfo<>(salvageList);
        return salvagePageInfo;
    }

    @Override
    public List<Guarantee> getGuaranteeList(List<String> dateList) {
        /**
         * @Description: 根据时间范围查找
         * @methodName: getGuaranteeList
         * @Param: [dateList]
         * @return: java.util.List<entity.Guarantee>
         * @Author: scyang
         * @Date: 2020/2/29 21:17
         */
        return guaranteeDao.getGuaranteeList2(dateList.get(1), dateList.get(0));
        // return guaranteeDao.getGuaranteeList(dateList);
    }

    @Override
    public List<String> addConsultationList(List<Consultation> consultationList) {
        /**
         * @Description: 批量添加合议信息
         * @methodName: addConsultationList
         * @Param: [consultationList]
         * @return: java.util.List<java.lang.String>
         * @Author: scyang
         * @Date: 2020/3/1 21:16
         */
        List<String> returnList = new ArrayList<>();
        if (!CollectionsUtils.isListEmpty(consultationList)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(2020 - 1900, 3 - 1, 1, 22, 9, 16));
            for (Consultation consultation : consultationList) {
                /** 添加主键 */
                consultation.setConsultationId(idWorker.nextId() + "");
                /** 合议人um账号 */
                consultation.setConsultationUm(("ex-".concat(consultation.getConsultationName()).toUpperCase().concat("001")));
                /** 合议人电话 */
                consultation.setPhone(StringUtils.midlleMask(consultation.getPhone(), 3, 4));
                /** 邮件顺序 */
                setMailOeder(consultationList);
                /** 增加合议信息并发送邮件 */
                Email email = new Email();
                /** 设置邮件发件人*/
                email.setSendUm(consultation.getConsultationUm());
                /** 设置邮件发送时间 */
                calendar.add(Calendar.MINUTE, 1);
                email.setSendDate(calendar.getTime());
                /** 获取收件人列表 */
                List<String> umList = consultationList.stream().map(a -> a.getConsultationUm()).collect(Collectors.toList());
                logger.info("umList{}:", umList);
                List<String> list = getUmList(umList);
                email.setToList(umList);
                email.setCcList(list);
                /** 设置收件人姓名 */
                String umName = getUmName(list);
                email.setToName(umName);
                email.setCcName(umName);
                /** 设置邮件主题 */
                String subject = getSubject(consultation.getConsultationType());
                email.setSubject(subject);
                /** 设置邮件类容 */
                setMessage(email, consultation);
                returnList.add(email.getMessage());
                emailDao.addEmail(email);
            }
            consultationDao.addConsultationList(consultationList);
        }
        return returnList;
    }

    @Override
    public List<Consultation> selectConsultation() {
        /**
         * @Description: 批量查询合议人员信息表
         * @methodName: selectConsultation
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Consultation>
         * @Author: scyang
         * @Date: 2020/3/2 22:33
         */

        return consultationDao.selectConsultation();
    }

    @Override
    public void addUserList(User[] users) {
        /**
         * @Description: 批量添加用户
         * @methodName: addUserList
         * @Param: [users]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/4 21:44
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2020 - 1900, 3 - 1, 5, 21, 49));
        for (User user : users) {
            user.setUserId(idWorker.nextId() + "");
            user.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));
            calendar.add(Calendar.MINUTE, -1);
            user.setRegDate(calendar.getTime());
            user.setPhone(StringUtils.middleSuff(user.getPhone(), 3, 4));
        }
        userDao.addUserArray(users);
    }

    @Override
    public User findUserName(String userName) {
        /**
         * @Description: 用户登录
         * @methodName: findUserName
         * @Param: [userName]
         * @return: com.tensquare.article.pojo.User
         * @Author: scyang
         * @Date: 2020/3/4 23:09
         */

        return userDao.login(userName);
    }

    @Override
    public List<User> selectUserList() {
        /**
         * @Description: 批量查询用户
         * @methodName: selectUserList
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.User>
         * @Author: scyang
         * @Date: 2020/3/4 23:56
         */
        return userDao.selectUserList();
    }

    @Override
    public String createCode(String iphone) {
        /**
         * @Description: 生成验证码
         * @methodName: createCode
         * @Param: [phone]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2020/3/5 21:25
         */
        String message = null;
        try {
            int code = numUtils.getNum(6);
            /** activeMQ发送消息 */
            Map<String, Object> map = new HashMap<>();
            map.put("iphone", iphone);
            map.put("code", code);
            logger.info("map{}:" + JSON.toJSONString(map));
            jmsTemplate.send("code", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("map", JSON.toJSONString(map));
                    return mapMessage;
                }
            });
            message = "验证码生成成功";
        } catch (JmsException e) {
            try {
                throw new ClaimpptException("生成验证码失败");
            } catch (ClaimpptException e1) {
                logger.info("e1{}:" + e1.getMessage());
                message = e1.getMessage();
            }
        }
        return message;
    }

    @Override
    public void registerUser(User user, String code) throws ClaimpptException {
        /**
         * @Description: 用户注册
         * @methodName: registerUser
         * @Param: [user, code]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/5 23:03
         */
        String redisCode = (String) redisTemplate.opsForValue().get(user.getPhone());
        logger.info("redisCode{}:" + redisCode);
        if (StringUtils.isEmpyStr(redisCode)) {
            throw new ClaimpptException("验证码不存在,请生成验证码...");
        }
        if (!StringUtils.isEqualTwoStr(code, redisCode)) {
            throw new ClaimpptException("验证码不正确,请认真核对验证码...");
        }
        user.setUserId(idWorker.nextId() + "");
        user.setPhone(StringUtils.midlleMask(user.getPhone(), 3, 4));
        user.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));
        user.setRegDate(new Date());
        user.setBirthday(new Date(1991 - 1900, 10 - 1, 16, 05, 25, 34));
        userDao.registerUser(user);
    }

    @Override
    public List<Province> selectAllProvince() {
        /**
         * @Description: 查询所有的省份信息
         * @methodName: selectAllProvince
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Province>
         * @Author: scyang
         * @Date: 2020/3/8 10:04
         */

        return provinceDao.selectAllProvince();
    }

    @Override
    public void addListCity(List<Map> mapList) {
        /**
         * @Description: 批量添加城市信息
         * @methodName: addListCity
         * @Param: [mapList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/8 11:15
         */
        if (!CollectionsUtils.isListEmpty(mapList)) {
            Calendar calendar = Calendar.getInstance();
            for (Map map : mapList) {
                map.put("cityId", idWorker.nextId() + "");
                map.put("provinceId", idWorker.nextId() + "");
                Date cityDate = JSON.parseObject(JSON.toJSONString(map)).getDate("cityDate");
                // Date cityDate = (Date) map.get("cityDate");
                calendar.setTime(cityDate);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                calendar.set(Calendar.MINUTE, 8);
                calendar.set(Calendar.SECOND, 8);
                map.put("cityDate", calendar.getTime());
                Map<String, Object> provinceMap = (Map<String, Object>) map.get("province");
                // Province province = JSON.parseObject(JSON.toJSONString(map.get("province"))).toJavaObject(Province.class);
                Province province = JSON.parseObject(JSON.toJSONString(map)).getJSONObject("province").toJavaObject(Province.class);
                // Province province = (Province) map.get("province");//报错
                logger.info("province{}:" + JSON.toJSONString(province));
                //Map provinceMap = JSON.parseObject(JSON.toJSONString(province),Map.class);
                provinceMap.put("provinceId", map.get("provinceId"));
                Date provinceDate = JSON.parseObject(JSON.toJSONString(provinceMap)).getDate("provinceDate");
                //Date provinceDate = (Date) provinceMap.get("provinceDate");
                calendar.setTime(provinceDate);
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                calendar.set(Calendar.MINUTE, 18);
                calendar.set(Calendar.SECOND, 18);
                provinceMap.put("provinceDate", calendar.getTime());
                //provinceDao.addProvince2(provinceMap);
                province.setProvinceId((String) map.get("provinceId"));
                province.setProvinceDate(calendar.getTime());
                provinceDao.addProvince(province);
            }
            cityDao.addCityList(mapList);
        }
    }

    @Override
    public void addListCity2(String paramJson) {
        JSONArray jsonArray = JSON.parseObject(paramJson).getJSONArray("cityList");
        List<City> cityList = jsonArray.toJavaList(City.class);
        if (!CollectionsUtils.isListEmpty(cityList)) {
            for (int i = 0; i < cityList.size(); i++) {
                cityList.get(i).setCityId(idWorker.nextId() + "");
                cityList.get(i).setProvinceId(idWorker.nextId() + "");
                Province province = jsonArray.getJSONObject(i).getJSONObject("province").toJavaObject(Province.class);
                province.setProvinceId(cityList.get(i).getProvinceId());
                provinceDao.addProvince(province);
            }
            cityDao.saveCityList(cityList);
        }
    }

    @Override
    public void deleteCityByIds(List<String> asList) {
        for (String cityId : asList) {
            /** 通过城市id获取省份id */
            Map<String, Object> map = cityDao.getProvinceIdByCityId(cityId);
            logger.info("map{}:" + JSON.toJSONString(map));
            String provinceId = (String) map.get("province_id");
            /** 根据省份id删 */
            provinceDao.deleteProvinceById(provinceId);
        }
        cityDao.delectByProvinceId(asList);
    }

    @Override
    public void updateCityList(String paramJson) {
        JSONArray jsonArray = JSON.parseObject(paramJson).getJSONArray("cityList");
        List<City> cityList = jsonArray.toJavaList(City.class);
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < cityList.size(); i++) {
            /** 根据cityName查出cityId和provinceId,cityName名字不能改变否则就为null */
            Map<String, Object> map = cityDao.getCityIdAndProvinceIdByCityName(cityList.get(i).getCityName());
            logger.info("map{}:" + JSON.toJSONString(map));
            /** 免得从前端传过来 */
            cityList.get(i).setCityId((String) map.get("city_id"));
            calendar.setTime(cityList.get(i).getCityDate());
            calendar.set(Calendar.HOUR_OF_DAY, 30);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 30);
            cityList.get(i).setCityDate(calendar.getTime());
            /** 先删除省份信息 */
            provinceDao.deleteProvinceById((String) map.get("province_id"));
            /** 添加城市信息 */
            Province province = jsonArray.getJSONObject(i).getJSONObject("province").toJavaObject(Province.class);
            /** 免得从前端传过来 */
            province.setProvinceId((String) map.get("province_id"));
            calendar.setTime(province.getProvinceDate());
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            calendar.set(Calendar.MINUTE, 15);
            calendar.set(Calendar.SECOND, 15);
            province.setProvinceDate(calendar.getTime());
            provinceDao.addProvince(province);
        }
        cityDao.updateCityList(cityList);
    }

    @Override
    public void addStudentList(List<Student> studentList) {
        /**
         * @Description: 批量添加学生信息系表(多对多)
         * @methodName: addStudentList
         * @Param: [studentList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/8 17:38
         */
        if (!CollectionsUtils.isListEmpty(studentList)) {
            for (Student student : studentList) {
                student.setStudentId(idWorker.nextId() + "");
                String desc = "{\"school\":\"应城一中\",\"class\":\"高二(3)班\"}";
                List<Teacher> teacherList = student.getTeacherList();
                for (Teacher teacher : teacherList) {
                    teacher.setTeacherId(idWorker.nextId() + "");
                    /** 设置中间表id */
                    //setTeacherIdAndStudentId(student.getStudentId(),teacher.getTeacherId());
                }
                /** 批量添加老师信息 */
                teacherDao.addTeacherList(teacherList);
                /** 设置中间表id */
                setTeacherIdAndStudentId2(student.getStudentId(), student.getTeacherList());
            }
            /** 批量添加学生信息 */
            studentDao.addStudentList(studentList);
        }
    }

    @Override
    public void deleteStudentByIds(String[] idArray) {
        /**
         * @Description: 批量删除学生信息
         * @methodName: deleteStudentByIds
         * @Param: [idArray]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/8 19:35
         */
        for (String studentId : idArray) {
            /** 通过中间表studentId获取teacherId(一对多) */
            List<Map<String, String>> mapList = teacherAndStudentDao.getTeacherIdByStudentId(studentId);
            for (Map<String, String> map : mapList) {
                String teacherId = map.get("teacher_id");
                logger.info("teacherId{}:" + teacherId);
                /** 删除老师表 */
                teacherDao.deleteTeacherById(teacherId);
            }
        }
        /** 批量删除中间表 */
        teacherAndStudentDao.deleteRelation(idArray);
        /** 批量删除学生表 */
        studentDao.deleteStudentByIds(idArray);
    }

    @Override
    public List<Student> selectAllStudent() {
        /**
         * @Description: 批量查询学生信息
         * @methodName: selectAllStudent
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.Student>
         * @Author: scyang
         * @Date: 2020/3/8 20:52
         */
        /** 学生表,中间表,老师表关联查询 */
        // List<Student> studentList=studentDao.selectAllStudent();
        /********************************************************************************/
        /** 先查询所有的学生列表 */
        List<Student> studentList = studentDao.selectStudent();
        logger.info("studentList{}:" + JSON.toJSONString(studentList));
        /** 再根据studentId查询中间表的teacherId */
        if (!CollectionsUtils.isListEmpty(studentList)) {
            // List<Teacher> teacherList=new ArrayList<>(); 查多了
            for (Student student : studentList) {
                List<Teacher> teacherList = new ArrayList<>();
                List<Map<String, String>> mapList = teacherAndStudentDao.getTeacherIdByStudentId(student.getStudentId());
                if (!CollectionsUtils.isListEmpty(mapList)) {
                    logger.info("mapList{}:" + JSON.toJSONString(mapList));
                    //List<Teacher> teacherList=new ArrayList<>(); 和上面一样
                    for (Map<String, String> map : mapList) {
                        /** 根据teacherId查询所有老师信息 */
                        Teacher teacher = teacherDao.selectTeacherById(map.get("teacher_id"));
                        teacherList.add(teacher);
                        logger.info("teacherList{}:" + JSON.toJSONString(teacherList));
                        student.setTeacherList(teacherList);
                    }
                }
            }
        }
        return studentList;
    }

    @Override
    public void updateStudentList(List<Student> studentList) {
        /**
         * @Description: 批量修改学生信息表(多对多)
         * @methodName: updateStudentList
         * @Param: [studentList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/8 22:09
         */
        if (!CollectionsUtils.isListEmpty(studentList)) {
            for (Student student : studentList) {
                /** 前端不想传studentId,通过studentName查询或得studentId(studentName不能发生改变) */
                logger.info("student{}:" + JSON.toJSONString(student));
               /* Map<String,Object> studentIdMap= studentDao.getStudentIdByStudentName(student.getStudentName());
                logger.info("studentIdMap{}:"+JSON.toJSONString(studentIdMap));
                String studentId = (String) studentIdMap.get("studentId");
                student.setStudentId(studentId);*/

                /** 通过studentId查询中间表获得teacherId */
                List<Map<String, String>> mapList = teacherAndStudentDao.getTeacherIdByStudentId(student.getStudentId());
                for (Map<String, String> map : mapList) {
                    /** 通过 teacherId 删除老师信息表 */
                    String teacherId = map.get("teacher_id");
                    teacherDao.deleteTeacherById(teacherId);
                }
                /** 删除中间表 */
                teacherAndStudentDao.deleteRelations(student.getStudentId());
                List<String> studentIdList = studentList.stream().map(a -> a.getStudentId()).collect(Collectors.toList());
                logger.info("studentIdList{}" + JSON.toJSONString(studentIdList));
                String[] idArray = new String[studentIdList.size()];
                String[] array = studentIdList.toArray(idArray);
                logger.info("array{}:" + JSON.toJSONString(array));
                // teacherAndStudentDao.deleteRelation(array); //删不完全
                /** 批量添加老师信息表 */
                List<Teacher> teacherList = student.getTeacherList();
                for (Teacher teacher : teacherList) {
                    teacher.setTeacherId(idWorker.nextId() + "");
                    /** 添加中间表 */
                    teacherAndStudentDao.addStudentIdAndTeacherId(student.getStudentId(), teacher.getTeacherId());
                }
                teacherDao.addTeacherList(teacherList);
            }
            /** 批量修改学生信息表 */
            studentDao.updateStudentListById(studentList);
        }
    }

    @Override
    public Map<String, Object> registerCode(String ipAddress) throws Exception {
        /**
         * @Description: 验证码注册
         * @methodName: registerCode
         * @Param: [ipAdress]
         * @return: java.util.Map<java.lang.String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                               java.lang.Object>
         * @Author: scyang
         * @Date: 2020/3/15 17:12
         */
        Map<String, Object> returnMap = null;
        String clientType = "web";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("client_type", clientType);
        paramMap.put("ip_address", ipAddress);
        returnMap = getRegisterCode(paramMap);

        return returnMap;
    }

    @Override
    public void addWeightLst(List<WeightSetting> weightList) {
        /**
         * @Description: 批量添加
         * @methodName: addWeightLst
         * @Param: [weightList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/31 21:35
         */
        /** 获取一个模式为BATCH,自动提交为lalse的Session */
        long start = System.currentTimeMillis();
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        logger.info("sqlSession{}" + sqlSession);
        WeightDao weightMapper = sqlSession.getMapper(WeightDao.class);
        try {
            logger.info("weightLst{}" + weightList.size());
            for (int i = 0; i < weightList.size(); i++) {
                WeightSetting weightSetting = weightList.get(i);
                logger.info("weightSetting{service层}:" + JSON.toJSONString(weightSetting));
                weightSetting.setWeightId(idWorker.nextId() + "");
                weightSetting.setWeightValue(setWeightValue(weightSetting.getWeightKey()));
                weightMapper.addWeight(weightSetting);
                if (i % 100 == 0 || i == weightList.size() - 1) {
                    /** 手动100个一提交,提交后无法回滚 */
                    sqlSession.commit();
                    /** 清理缓存,防止溢出 */
                    sqlSession.clearCache();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            /** 没有提交的数据可以回滚 */
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        logger.info("花费了:" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    public void addListCase(List<PreCase> caseList) {
        long start = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2020 - 1900, 3 - 1, 25, 15, 16, 17));
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        PreCaseDao precaseMapper = sqlSession.getMapper(PreCaseDao.class);
        try {
            for (int i = 0; i < caseList.size(); i++) {
                PreCase preCase = caseList.get(i);
                preCase.setPreCaseId(idWorker.nextId() + "");
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                preCase.setDownLineSchedul(setDownLineSchedul(preCase.getIsOnLine(), calendar.getTime()));
                preCase.setAccidentLevel(setAccidentLevel(preCase.getDeathPeople(), preCase.getHurtPeople()));
                precaseMapper.addCase(preCase);
                if (i % 100 == 0 || i == caseList.size() - 1) {
                    sqlSession.commit();
                    sqlSession.clearCache();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        logger.info("花费了:" + (System.currentTimeMillis() - start) + "毫秒");
    }

    @Override
    public void addOutBreakList(List<OutBreak> outBreakList) {
        /**
         * @Description: 添加疫情信息
         * @methodName: addOutBreakList
         * @Param: [outBreakList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/4/7 21:14
         */
        if (!CollectionsUtils.isListEmpty(outBreakList)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(2020 - 1900, 1 - 1, 29));
            for (OutBreak outBreak : outBreakList) {
                outBreak.setOutBreakId(idWorker.nextId() + "");
                outBreak.setArriveDate(calendar.getTime());
                outBreak.setLeaveDate(setLeaveDate(outBreak.getArriveDate(), outBreak.getSupportDays()));
                outBreak.setSubsidySum(outBreak.getSubsidyAmount().
                        multiply(new BigDecimal(outBreak.getNurseCount())).
                        setScale(2, BigDecimal.ROUND_HALF_DOWN));
                outBreak.setRate(setRate(outBreak.getSubsidyAmount(), outBreak.getSubsidySum()));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            outBreakDao.addOutBreakList(outBreakList);
        }
    }

    @Override
    public List<OutBreak> selectOutBreak() {
        /**
         * @Description: 批量查询疫情信息
         * @methodName: selectOutBreak
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pojo.OutBreak>
         * @Author: scyang
         * @Date: 2020/4/8 23:13
         */
        List<OutBreak> outBreakList = outBreakDao.selectOutBreak();
        /** 获取最晚抵达武汉的医疗队 */
        Date arriveDateMax = outBreakList.stream()
                .max((outBreakOne, outBreakTwo) ->
                        outBreakOne.getArriveDate().compareTo(outBreakTwo.getArriveDate())).get().getArriveDate();
        logger.info("arriveDateMax{}:" + arriveDateMax);
        /** 最后支援武汉的四家医疗队 */
        List<Date> dateList = new ArrayList<>();
        Date date = null;
        List<OutBreak> fourOutBreakList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(arriveDateMax);

        for (int i = 0; i < 4; i++) {
            date = calendar.getTime();
            dateList.add(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        logger.info("dateList{}:" + dateList);
        for (OutBreak outBreak : outBreakList) {
            Date arriveDate = outBreak.getArriveDate();
            for (Date date_ : dateList) {
                if (arriveDate.compareTo(date_) == 0) {
                    fourOutBreakList.add(outBreak);
                    break;
                }
            }
        }
        logger.info("fourOutBreakList{合并前}:" + JSON.toJSONString(fourOutBreakList));

        /** 排除这四家医疗队 */
        List<OutBreak> lessOutBreakList = new ArrayList<>(outBreakList);
        lessOutBreakList.removeAll(fourOutBreakList);
        logger.info("lessOutBreakList{}:" + JSON.toJSONString(lessOutBreakList));
        /** 剩余四支医疗队按支援的时间从小到大的排序 */

        logger.info("剩余四支医疗队按支援的时间从小到大的排序打印开始...");
        lessOutBreakList.stream().distinct().filter(
                outBreak -> !"武汉市".equals(outBreak.getSupportCity()))
                .sorted((outBreakOne, outBreakTwo) -> (int) (outBreakOne.getArriveDate().getTime()
                        - outBreakTwo.getArriveDate().getTime()))
                .collect(Collectors.toList()).forEach(System.out::println);
        logger.info("剩余四支医疗队按支援的时间从小到大的排序打印结束...");

        /** 补贴金额相等的合并,并移除掉 */
        for (int i = fourOutBreakList.size() - 1; i >= 0; i--) {
            BigDecimal subsidyAmountMove = fourOutBreakList.get(i).getSubsidyAmount();
            BigDecimal subsidySumMove = fourOutBreakList.get(i).getSubsidySum();
            for (int j = 0; j < i; j++) {
                BigDecimal subsidyAmountAdd = fourOutBreakList.get(j).getSubsidyAmount();
                BigDecimal subsidySumAdd = fourOutBreakList.get(j).getSubsidySum();
                if (subsidyAmountMove.compareTo(subsidyAmountAdd) == 0) {
                    subsidyAmountAdd = subsidyAmountAdd.add(subsidyAmountMove).setScale(2, BigDecimal.ROUND_HALF_UP);
                    subsidySumAdd = subsidySumAdd.add(subsidySumMove).setScale(2, BigDecimal.ROUND_HALF_UP);
                    fourOutBreakList.get(j).setSubsidyAmount(subsidyAmountAdd);
                    fourOutBreakList.get(j).setSubsidySum(subsidySumAdd);
                    fourOutBreakList.remove(i);
                    break;
                }
            }
        }
        logger.info("fourOutBreakList{合并后}:" + JSON.toJSONString(fourOutBreakList));

        /**************************************************************************************************************/
        /** 根据支援城市去重 */
        ArrayList<OutBreak> distinctOutBreakList = outBreakList.stream().collect(Collectors.
                collectingAndThen(Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(OutBreak::getSupportCity))),
                        ArrayList::new));
        logger.info("distinctOutBreakList{}:" + JSON.toJSONString(distinctOutBreakList));

        /** 根据支援城市分组 */
        Map<String, List<OutBreak>> groupOutBreakMap = outBreakList.stream().
                collect(Collectors.groupingBy(
                        outBreak -> outBreak.getSupportCity()));
        logger.info("groupOutBreakMap{}:" + JSON.toJSONString(groupOutBreakMap));
        /**************************************************************************************************************/

        return outBreakList;
    }

    @Override
    public List<PaymentItem> generatePayList(Settlenment settlenment, List<CoinsShare> coinsShareList) {
        /**
         * @Description: 生成赔款的支付共保清单
         * @methodName: generatePayList
         * @Param: [settlenment, coinsShareList]
         * @return: java.util.List<com.tensquare.article.pingan.PaymentItem>
         * @Author: scyang
         * @Date: 2020/4/12 9:02
         */
        List<PaymentItem> resList = new ArrayList<>();
        String paySign = settlenment.getPaySign();
        if (StringUtils.isEqualTwoStr(paySign, Constant.COMMON_N)) {
            return resList;
        }
        List<PaymentItem> paymentItemList = settlenment.getPaymentItemList();
        /** 只做处理赔款,预赔的*/
        List<PaymentItem> tempList = new ArrayList<>();
        for (PaymentItem paymentItem : paymentItemList) {
            if (Constant.PAYMENT_TYPE_LIST.contains(paymentItem.getPayType())) {
                tempList.add(paymentItem);
            }
        }
        logger.info("tempList{}:" + JSON.toJSONString(tempList));
        if (!CollectionsUtils.isListEmpty(tempList)) {
            resList = getPaymentItemList(tempList, coinsShareList);
            logger.info("resList{}:" + JSON.toJSONString(resList));
        }
        return resList;
    }

    @Override
    public void createNotice(MsgNotice msgNotice) throws ClaimpptException {
        /**
         * @Description: 发送消息
         * @methodName: createNotice
         * @Param: [msgNotice]
         * @return: void
         * @Author: scyang
         * @Date: 2020/4/25 11:26
         */

        /** 先从redis中查找 */
        String redisKey = "redis_key" + msgNotice.getSender();
        Integer num = (Integer) redisTemplate.opsForValue().get(redisKey);
        logger.info("num{}:" + num);
        if (num == null) {
            num = 1;
            /** 存入redis中 */
            redisTemplate.opsForValue().set(redisKey, num, 2, TimeUnit.MINUTES);
        }
        if (num <= 10) {
            msgNotice.setNoticeId(idWorker.nextId() + "");
            msgNotice.setCreateDate(new Date());
            msgNoticeDao.createNotice(msgNotice);
            num++;
            redisTemplate.opsForValue().set(redisKey, num, 2, TimeUnit.MINUTES);
        } else {
            throw new ClaimpptException("2分钟类最多只能发送10条消息...");
        }
    }

    @Override
    public void updateNotice(List<String> idsList, String status) {
        /**
         * @Description: 批量修改消息状态
         * @methodName: updateNotice
         * @Param: [idsList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/4/25 14:29
         */
        msgNoticeDao.updateNotice(idsList, status);
    }

    @Override
    public void addEmploverList(List<Employer> emploverList) {
        /**
         * @Description: 批量添加雇员信息
         * @methodName: addEmploverList
         * @Param: [emploverList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/4/25 15:46
         */
        Calendar instance = Calendar.getInstance();
        for (Employer employer : emploverList) {
            employer.setEmploverId(idWorker.nextId() + "");
            employer.setNames(StringUtils.renameMask(employer.getNames()));
            employer.setIdCard(StringUtils.idCardMask(employer.getIdCard()));
            employer.setSeriaNo(DateUtil.DateToStr(new Date(), DateUtil.FORMATFIVE) + numUtils.getNum(6));
            employer.setEmail(StringUtils.mailMask(employer.getEmail()));
            employer.setStartDate(instance.getTime());
            employer.setEndDate(DateUtil.addDate(employer.getStartDate(), 5));
            employer.setTypes(setTypes(employer.getTypes()));
            instance.add(Calendar.DAY_OF_MONTH, 10);
        }
        emploverList.stream().forEach(employer -> emploverDao.addEmplover(employer));
    }

    @Override
    public List<Employer> selectEmplover() {
        /**
         * @Description: 批量查询雇员信息
         * @methodName: selectEmplover
         * @Param: []
         * @return: java.util.List<com.tensquare.article.pingan.Employer>
         * @Author: scyang
         * @Date: 2020/4/25 17:27
         */
        List<Employer> employerList = emploverDao.selectEmplover();
        Map<String, List<Employer>> listMap = employerList.stream().distinct().collect(Collectors.groupingBy(emplover -> emplover.getTypes()));
        logger.info("listMap{}:" + JSON.toJSONString(listMap));
        /*for (Employer employer : employerList) {
            employer.setDataMap(listMap);
        }*/
        //employerList.stream().forEach(employer->employer.setDataMap(listMap));
        return employerList;
    }

    @Override
    public void addImageList(List<Image> imageList) {
        /**
         * @Description: 批量添加图片信息
         * @methodName: addImageList
         * @Param: [imageList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/5/5 13:58
         */
        String imageKey = SwitchFetchUtils.getStringSwitchValue("imageKey",IMAGEKEY);
        List<String> typeList = Arrays.asList(imageKey.split(","));
        logger.info("typeList{}:"+typeList);
        if (!CollectionsUtils.isListEmpty(imageList)) {
            for (Image image : imageList) {
                image.setImageId(idWorker.nextId() + "");
                image.setCreateDate(new Date());
                image.setImageName(setImageName(image.getImageName()));
                int index = image.getImageName().lastIndexOf(".");
                if (index != -1) {
                    image.setImageEnd(image.getImageName().substring(index + 1));
                }
                image.setImageType(setImageType(image.getImageEnd(),typeList));
                image.setImageSize("("+image.getImageSize()+")"+page(imageList,image)+"张");
            }
            imageDao.addImageList(imageList);
        }
    }

    @Override
    public List<Map<String, Object>> getListImage(String[] idList,String startDate,String endDate) {
        /**
         * @Description: 批量查询图片信息
         * @methodName: getListImage
         * @Param: [idList]
         * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
         * @Author: scyang
         * @Date: 2020/5/7 20:43
         */
        List<Map<String, Object>> listImage = imageDao.getListImage(idList, startDate, endDate);
        /** 发送activeMq消息 */
        jmsTemplate.send("imageMq", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                String textMsg = JSON.toJSONString(listImage);
                return session.createTextMessage(textMsg);
            }
        });
        return listImage;
    }

    @Override
    public List<String> getDateStrList() {
        return imageDao.getDateStrList();
    }

    @Override
    public void addListDuty(List<DutyPerson> dutyPersonList, List<Duty> dutyList) {
        /**
         * @Description: 批量添加纳税人信息
         * @methodName: addListDuty
         * @Param: [dutyPersonList, dutyList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/5/10 16:42
         */
        int num=2;
        for (DutyPerson dutyPerson : dutyPersonList) {
            String idCard_person = dutyPerson.getIdCard();
            for (Duty duty : dutyList) {
                String idCard_duty = duty.getIdCard();
                if (idCard_person.equals(idCard_duty)){
                    dutyPerson.setDutyPersonId(idWorker.nextId()+"");
                    duty.setDuityId(idWorker.nextId()+"");
                    /** 税务局名称 */
                    duty.setDutyName("国家税务总局深圳市龙华区税务局");
                    /** 任职受雇单位 */
                    duty.setWorkCompany(dutyPerson.getWorkCompany());
                    /** 缴纳起始年度 */
                    duty.setPayStart("2019-01");
                    /** 缴纳终止年度 */
                    duty.setPayEnd("2019-12");
                    /** 申请时间 */
                    duty.setApplyDate(setOthenDate(duty.getApplyDate(),num));
                    /** 审核的时间 */
                    duty.setAuditDate(setOthenDate(duty.getAuditDate(),num));
                    /** 咨询时间 */
                    duty.setAskDate(setOthenDate(duty.getAskDate(),num ));
                    /** 咨询结果 */
                    duty.setAskResult(getAskResult(duty.getAskDate()));
                    /** 申请到审核之间的天数 */
                    duty.setApplyAuditDays(new BigDecimal(DateUtil.getBetweenDay(duty.getApplyDate(), duty.getAuditDate())));
                   /** 审核状态 */
                   duty.setAuditStatus("1");
                   /** 国库处理时间 */
                   duty.setDealDate(setOthenDate(duty.getDealDate(),num ));
                   /** 审核到国库处理的天数 */
                   duty.setAuditDealDays(new BigDecimal(DateUtil.getBetweenDay(duty.getAuditDate(),duty.getDealDate())));
                   /** 处理状态 */
                   duty.setDealStatus("1");
                   /** 收入合计 */
                   duty.setSalaryCount(dutyPerson.getSalaryAmount().multiply(new BigDecimal(12)));
                   /** 已纳税额 */
                    duty.setTaxPayAmount((duty.getSalaryCount().subtract(new BigDecimal(60000.00))).multiply(new BigDecimal(0.03)));
                    /** 减税项目 */
                    duty.setFreeDutyName(setFreeDutyName(dutyPerson.getFreeDutyName()));
                    /** 免税收入 */
                    duty.setFreeDutyAmount(setFreeDutyAmount(duty.getFreeDutyName()));
                    /** 退税补税差价 */
                    duty.setDutySubtractAmount(setDutySubtractAmount(duty.getFreeDutyAmount(),duty.getTaxPayAmount()));
                    /** 设置退税补税状态 */
                    duty.setDutyStatus("1");
                    dutyPersonDao.addDutyPerson(dutyPerson);
                    dutyDao.addDuty(duty);
                    break;
                }
            }
            num++;
        }
    }

    @Override
    public void addCaseTeamList(List<CaseTeam> caseTeamList) {
        /**
         * @Description: 批量添加重案信息
         * @methodName: addCaseTeamList
         * @Param: [caseTeamList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/5/12 20:12
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2020-1900,5-1,12,20,30,25));
        for (CaseTeam caseTeam : caseTeamList) {
            caseTeam.setCaseTeamId(idWorker.nextId()+"");
            caseTeam.setCaseTeamValue(getCaseValue(caseTeam.getCaseTeamStatus()));
            caseTeamDao.addCaseTeam(caseTeam);
            for (CasePerson casePerson : caseTeam.getCasePersonList()) {
                casePerson.setCasePersonId(idWorker.nextId()+"");
                casePerson.setCaseTeamId(caseTeam.getCaseTeamId());
                casePerson.setCreateDate(calendar.getTime());
                casePerson.setTotalScore(casePerson.getContributeScore().add(casePerson.getEffectiveScore()).add(casePerson.getServiceScore()));
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                calendar.add(Calendar.MINUTE, -5);
                calendar.add(Calendar.SECOND, 10);
                casePersonDao.addCasePerson(casePerson);
            }
        }
    }

    @Override
    public Map<String, Object> selectCasePersonList() {
       List<CasePerson> casePersonList= casePersonDao.selectCasePersonList();
        logger.info("casePersonList{}:"+JSON.toJSONString(casePersonList));
        Map<String, Object> paramMap=new HashMap<>();
        /** 总分从大到小顺序排列 */
        List<BigDecimal> totalScoreList = casePersonList.stream()
                .map(CasePerson::getTotalScore)
                .sorted((totalOne, totalTwo) -> totalTwo.compareTo(totalOne))
                .collect(Collectors.toList());
        logger.info("totalScoreList{}:"+JSON.toJSONString(totalScoreList));
        Map< BigDecimal,String> score_name_map = casePersonList.stream().collect(Collectors.toMap( CasePerson::getTotalScore,CasePerson::getCasePersonName));
       logger.info("name_score_map{}:"+JSON.toJSONString(score_name_map));

        totalScoreList = score_name_map.keySet()
                .stream()
                .sorted((totalOne, totalTwo) -> totalTwo.compareTo(totalOne))
                .collect(Collectors.toList());

        List<String> nameList= casePersonList.stream().map(CasePerson::getCasePersonName).collect(Collectors.toList());
        logger.info("nameList{}:"+JSON.toJSONString(nameList));

        Map<String,Object> allTotalScoreMap=new HashMap<>();
        for (int i = 0; i < totalScoreList.size(); i++) {
            BigDecimal totalScore = totalScoreList.get(i);
            for (String casePersonName : nameList) {
                if (score_name_map.get(totalScore).equals(casePersonName)){
                    allTotalScoreMap.put(casePersonName+"在全国排名是",i+1+"名" );
                    break;
                }
            }
        }

        logger.info("allTotalScoreMap{}:"+JSON.toJSONString(allTotalScoreMap));
        paramMap.put("allTotalScoreMap",allTotalScoreMap );

        return paramMap;
    }

    private String getCaseValue(String caseTeamStatus) {
        switch (caseTeamStatus){
            case "0":
                return "不在线";
            case "1":
                return "在线";
             }
        return null;
    }

    private BigDecimal setDutySubtractAmount(BigDecimal freeDutyAmount, BigDecimal taxPayAmount) {
        BigDecimal decimal = taxPayAmount.subtract(freeDutyAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (decimal.compareTo(BigDecimal.ZERO)>=0){
            return decimal;
        }
        return taxPayAmount;
    }

    private BigDecimal setFreeDutyAmount(String freeDutyName) {
        BigDecimal subtractAmount=BigDecimal.ZERO;
        JSONObject object = JSON.parseObject(freeDutyName);
        String father = object.getString("father");
        String mother = object.getString("mother");
        String son = object.getString("son");
        String renting = object.getString("renting");
        if (father!=null||mother!=null){
            subtractAmount=subtractAmount.add(new BigDecimal(12000));
        }
        if (!StringUtils.isEmpyStr(son)){
            subtractAmount=subtractAmount.add(new BigDecimal(10000));
        }
        subtractAmount=subtractAmount.add(new BigDecimal(8000));
        logger.info("subtractAmount:{}:"+subtractAmount);
        return subtractAmount;
    }

    private String setFreeDutyName(String freeDutyName) {
        JSONObject json=new JSONObject();

       // {"parents":[{"father":"422202199109093496"},{"mother":"422202199109093496"}],"children":{"son":"422202199109093496"},"renting":"在深圳租房"}
        JSONObject jsonObject = JSON.parseObject(freeDutyName);
        String father_id_card = jsonObject.getJSONArray("parents").getJSONObject(0).getString("father");
        logger.info("father_id_card{}:"+father_id_card);
        String mother_id_card = jsonObject.getJSONArray("parents").getJSONObject(1).getString("mother");
        logger.info("mother_id_card{}:"+mother_id_card);
        String son_id_card = jsonObject.getJSONObject("children").getString("son");
        logger.info("son_id_card{}:"+son_id_card);
        String renting = jsonObject.getString("renting");
        logger.info("renting{}:"+renting);
        try {
            if (StringUtils.getAgeFromIdCard(father_id_card)>=60){
                json.put("father", father_id_card);
            }
            if (StringUtils.getAgeFromIdCard(mother_id_card)>=60){
                json.put("mother", mother_id_card);
            }
            if (StringUtils.getAgeFromIdCard(son_id_card)>=10){
                json.put("son", son_id_card);
            }
            json.put("renting", renting);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("json{}:"+JSON.toJSONString(json));
        return JSON.toJSONString(json);
    }

    private String getAskResult(Date askDate)  {
        String result="";
        try {
            if (!DateUtil.isWeekDay(askDate)||!DateUtil.isBetweenDate(askDate, "09:00:00", "18:00:00")){
                result= "不在工作日时间或者不在上班时间不予支持人工服务";
            }
            if (DateUtil.isWeekDay(askDate)&&DateUtil.isBetweenDate(askDate, "09:00:00", "18:00:00")){
                result= "正在受理请耐心等待";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Date setOthenDate(Date applyDate, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(applyDate);
        calendar.add(Calendar.HOUR_OF_DAY, 8+num);
        calendar.add(Calendar.MINUTE, 12-num);
        calendar.add(Calendar.SECOND, 30+num);
        return calendar.getTime();
    }

    private String page(List<Image> imageList,Image image) {
        List<Image> gifList=new ArrayList<>();
        List<Image> jpgList=new ArrayList<>();
        List<Image> dxfList=new ArrayList<>();
        List<Image> pptList=new ArrayList<>();
        for (Image image2 : imageList) {
            if (image2.getImageEnd()!=null){
                if (image2.getImageEnd().endsWith("gif")){
                    gifList.add(image2);
                }
                if (image2.getImageEnd().endsWith("jpg")){
                    jpgList.add(image2);
                }
                if (image2.getImageEnd().endsWith("dxf")){
                    dxfList.add(image2);
                }
                if (image2.getImageEnd().endsWith("ppt")){
                    pptList.add(image2);
                }
            }
        }
       logger.info("gifList{}:"+gifList.size());
       logger.info("jpgList{}:"+jpgList.size());
       logger.info("dxfList{}:"+dxfList.size());
       logger.info("pptList{}:"+pptList.size());
        return pageSize(gifList,jpgList,dxfList,pptList,image);
    }

    private String pageSize(List<Image> gifList, List<Image> jpgList, List<Image> dxfList, List<Image> pptList,Image image) {
        switch (image.getImageEnd()){
            case "gif":
                return String.valueOf(gifList.size());
            case "jpg":
                return String.valueOf(jpgList.size());
            case "dxf":
                return String.valueOf(dxfList.size());
                default:
                    return String.valueOf(pptList.size());
             }
    }

    private String setImageType(String imageEnd,List<String> typeList) {
        String type = "";
        switch (imageEnd) {
            case "gif":
                type = typeList.get(0);
                break;
            case "jpg":
                type = typeList.get(1);
                break;
            case "dxf":
                type = typeList.get(2);
                break;
            default:
                type = "不知道是什么类型";
        }
        return type;
    }

    private String setImageName(String imageName) {
        switch (imageName) {
            case "gif":
                return UUID.randomUUID() + "." + imageName;
            case "jpg":
                return UUID.randomUUID() + "." + imageName;
            case "dxf":
                return UUID.randomUUID() + "." + imageName;
                default:
                    return UUID.randomUUID() + "." + imageName;
        }
        //return null;
    }


    private String setTypes(String types) {
        String str = "";
        switch (types) {
            case "0":
                str = "在线";
                break;
            case "1":
                str = "下线";
                break;
            default:
                str = "不知道";
        }
        return str;
    }

    private List<PaymentItem> getPaymentItemList(List<PaymentItem> tempList, List<CoinsShare> coinsShareList) {
        /** 赔款总金额 */
        BigDecimal totalCoinsShareAmount = BigDecimal.ZERO;
        for (CoinsShare coinsShare : coinsShareList) {
            if (StringUtils.isEqualTwoStr(Constant.COMMON_Y, coinsShare.getPayType())) {
                totalCoinsShareAmount = totalCoinsShareAmount.add(coinsShare.getCoinsShareAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        logger.info("totalCoinsShareAmount{}:" + totalCoinsShareAmount);
        /** 计算同一笔赔款的支付合计金额 */
        BigDecimal payAmountSum = BigDecimal.ZERO;
        for (PaymentItem paymentItem : tempList) {
            payAmountSum = payAmountSum.add(paymentItem.getPaymentAmount() == null ? BigDecimal.ZERO : paymentItem.getPaymentAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        logger.info("totalCoinsShareAmount{}:" + totalCoinsShareAmount);
        if (payAmountSum.compareTo(BigDecimal.ZERO) == 0) {
            payAmountSum = BigDecimal.ONE;
        }
        /** 支付金额合计 */
        BigDecimal tempAmountSum = BigDecimal.ZERO;
        /** 汇率合计 */
        BigDecimal tempRateSum = BigDecimal.ZERO;
        for (PaymentItem paymentItem : tempList) {
            /** 支付金额 */
            BigDecimal amount = BigDecimal.ZERO;
            /** 汇率 */
            BigDecimal rate = BigDecimal.ZERO;
            rate = paymentItem.getPaymentAmount().divide(payAmountSum, 2, BigDecimal.ROUND_HALF_UP);
            logger.info("rate{}:" + rate);
            amount = totalCoinsShareAmount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
            logger.info("amount{}:" + amount);
            tempRateSum = tempRateSum.add(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
            tempAmountSum = tempAmountSum.add(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        logger.info("tempRateSum{}:" + tempRateSum);
        logger.info("tempAmountSum{}:" + tempAmountSum);
        return tempList;
    }

    private String setRate(BigDecimal subsidyAmount, BigDecimal subsidySum) {
        /** 设置百分比 */
        BigDecimal divide = subsidyAmount.divide(subsidySum, 6, BigDecimal.ROUND_HALF_UP);
        logger.info("divide{}:" + divide);
        DecimalFormat decimalFormat = new DecimalFormat("0.0000%");
        return decimalFormat.format(divide);
    }

    private Date setLeaveDate(Date arriveDate, BigDecimal supportDays) {
        /**
         * @Description: 设置撤离时间
         * @methodName: setLeaveDate
         * @Param: [arriveDate, supportDays]
         * @return: java.util.Date
         * @Author: scyang
         * @Date: 2020/4/7 21:24
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(arriveDate);
        calendar.add(Calendar.DAY_OF_MONTH, Integer.valueOf(String.valueOf(supportDays)));
        return calendar.getTime();
    }

    private String setAccidentLevel(String deathPeople, String hurtPeople) {
        Integer deathPeopleInt = Integer.valueOf(deathPeople);
        Integer hurtPeopleInt = Integer.valueOf(hurtPeople);
        String accidentLevel = "";
        if (deathPeopleInt > 30 || hurtPeopleInt > 100) {
            accidentLevel = "特别重大事故";
        } else if ((deathPeopleInt >= 10 && deathPeopleInt <= 30) || (hurtPeopleInt >= 50 && hurtPeopleInt <= 100)) {
            accidentLevel = "重大事故";

        } else if ((deathPeopleInt >= 3 && deathPeopleInt < 10) || (hurtPeopleInt >= 10 && hurtPeopleInt < 50)) {
            accidentLevel = "较大事故";

        } else if (deathPeopleInt < 3 || hurtPeopleInt < 10) {
            accidentLevel = "一般事故";

        }
        return accidentLevel;
    }

    private String setDownLineSchedul(String isOnLine, Date date) {

        String downLineSchedul = "";
        if ("1".equals(isOnLine)) {
            downLineSchedul = "已在线,无需展开下线计划";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            downLineSchedul = DateUtil.DateToStr(date, DateUtil.FORMATTWO) + "--" + DateUtil.DateToStr(calendar.getTime(), DateUtil.FORMATTWO);
        }
        return downLineSchedul;
    }

    private String setWeightValue(String weightKey) {
        String weightValue = "";
        switch (weightKey) {
            case "1.2":
                weightValue = Constant.FOLDING_UPPER_WARN;
                break;
            case "1.0":
                weightValue = Constant.FOLDING_UPPER;
                break;
            case "0.75":
                weightValue = Constant.FOLDING_AVG;
                break;
            case "0.5":
                weightValue = Constant.FOLDING_FLOOR;
                break;
            case "0.25":
                weightValue = Constant.FOLDING_FLOOR_WARN;
                break;
        }
        logger.info("weightValue{}:" + weightValue);
        return weightValue;
    }

    private Map<String, Object> getRegisterCode(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            /** 初始化注册验证码流程 */
            paramMap.put("gt", captchaId);
            JSONObject jsonObject = getOpenApi(REGISTER_URL, paramMap);
            if (StringUtils.isEqualTwoStr(jsonObject.getString("ret"), "0")) {
                JSONObject parseObject = JSONObject.parseObject(StringUtils.transferNulltoStr(jsonObject.get("data")));
                String challenge = parseObject.getString("challenge");
                if (challenge.length() == 32) {
                    String md5Encode = Md5Utils.MD5Encode(challenge);
                    resultMap.put("success", 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("success", 0);
        return resultMap;
    }

    private JSONObject getOpenApi(String url, Map<String, Object> paramMap) {
        /** 参数拼接url,并增加accessToken */
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb = sb.deleteCharAt(sb.lastIndexOf("&"));
        url = url + "?" + sb;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("utf-8")));
        httpHeaders.setAccept(Arrays.asList(new MediaType[]{new MediaType("application", "json", Charset.forName("utf-8"))}));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        return (JSONObject) exchange(url, HttpMethod.GET, httpEntity, String.class);
    }

    private JSONObject postOpenApi(String url, Map<String, Object> paramMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("utf-8")));
        httpHeaders.setAccept(Arrays.asList(new MediaType[]{new MediaType("application", "json", Charset.forName("utf-8"))}));
        String body = JSONObject.toJSONString(paramMap);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
        return (JSONObject) exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    private JSONObject exchange(String url, HttpMethod method, HttpEntity<String> httpEntity, Class<String> uriVariables) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, method, httpEntity, String.class, uriVariables);
        JSONObject jsonObject = JSONObject.parseObject(exchange.getBody());
        String code = StringUtils.canelNullObj(jsonObject.get("ret"));
        /**
         * 必须判断access_token是否失效,失效要重新获取一次token再调用
         * code值: 13002:非法的access_token,13012:已失效的access_token,14005:url地址受限
         */
        if ("13002".equals(code) || "13012".equals(code) || "14005".equals(code)) {
            String tokenResult = restTemplate.getForObject(REGISTER_URL, String.class);
            exchange = restTemplate.exchange(url, method, httpEntity, String.class, uriVariables);
            jsonObject = JSONObject.parseObject(exchange.getBody());
        }
        return jsonObject;
    }

    private void setTeacherIdAndStudentId(String studentId, String teacherId) {
        studentDao.addStudentIdAndTeacherId(studentId, teacherId);
    }

    private void setTeacherIdAndStudentId2(String studentId, List<Teacher> teacherList) {
        for (Teacher teacher : teacherList) {
            teacherAndStudentDao.addStudentIdAndTeacherId(studentId, teacher.getTeacherId());
        }
    }

    private void setMessage(Email email, Consultation consultation) {
        String message = "";
        switch (consultation.getConsultationType()) {
            case Constant.CONSULTATION_TYPE_REGISTER:
                message = "立案合议的邮件,请注意查收";
                break;
            case Constant.CONSULTATION_TYPE_SETTLE:
                message = "结案合议的邮件,请注意查收";
                break;
            case Constant.CONSULTATION_TYPE_COURSE:
                message = "过程合议的邮件,请注意查收";
                break;
            case Constant.CONSULTATION_TYPE_ALIPAY:
                message = "支付合议的邮件,请注意查收";
                break;
            default:
                message = "";
        }
        logger.info("message{}:", message);
        email.setMessage(message);
    }

    private String getSubject(String consultationType) {
        String subject = "";
        switch (consultationType) {
            case Constant.CONSULTATION_TYPE_REGISTER:
                subject = "【立案合议】";
                break;
            case Constant.CONSULTATION_TYPE_SETTLE:
                subject = "【结案合议】";
                break;
            case Constant.CONSULTATION_TYPE_COURSE:
                subject = "【过程合议】";
                break;
            case Constant.CONSULTATION_TYPE_ALIPAY:
                subject = "【支付合议】";
                break;
            default:
                subject = "";
        }
        logger.info("subject{}:", subject);
        return subject;
    }

    private String getUmName(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String name : list) {
            sb.append(name).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        logger.info("sb{}:", sb);
        return sb.toString();
    }

    private List<String> getUmList(List<String> umList) {
        List<String> list = new ArrayList<>();
        for (String um : umList) {
            if (!StringUtils.isEmpyStr(um)) {
                list.add(um.concat("@pingan.com.cn"));
            }
        }
        logger.info("list{}:", list);
        return list;
    }

    private void setMailOeder(List<Consultation> consultationList) {
        /**
         * @Description: 设置邮件顺序
         * @methodName: setMailOeder
         * @Param: [consultationList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/3/1 21:26
         */
        for (int i = 0; i < consultationList.size(); i++) {
            String mailOrder = String.valueOf(i + 1);
            logger.info("mailOrder{}:", mailOrder);
            consultationList.get(i).setMailOrder(mailOrder);
        }
    }

    private String getSalvageWeb(String salvageName) {
        /** 获取残值网址 */
        String salvageWeb = "";
        switch (salvageName) {
            case "中国建设银行":
                salvageWeb = CCB;
                break;
            case "中国农业银行":
                salvageWeb = ABC;
                break;
            case "中国工商银行":
                salvageWeb = ICBC;
                break;
            case "中国招商银行":
                salvageWeb = CMB;
                break;
        }
        logger.info("salvageWeb{}:" + salvageWeb);
        return salvageWeb;
    }

    private String getSalvageDesc(String salvageName) {
        /** 获取残值描叙 */
        String salvageDesc = "";
        List<String> list = new ArrayList<>();
        Collections.addAll(list, CCB, ABC, ICBC, CMB);
        logger.info("list的长度size{}：" + list.size());
        switch (salvageName) {
            case "中国建设银行":
                salvageDesc = getString("京ICP备13030780号") + ",建行平台";
                break;
            case "中国农业银行":
                salvageDesc = getString("京ICP备13030760号") + ",农行平台";
                break;
            case "中国工商银行":
                salvageDesc = getString("京ICP备13030770号") + ",工行平台";
                break;
            case "中国招商银行":
                salvageDesc = getString("京ICP备13030750号") + ",招行平台";
                break;
        }
        //logger.info("salvageDesc{}:"+salvageDesc);
        return salvageDesc;
    }

    private String getString(String salvageDesc) {

        //salvageDesc="京ICP备13030780号";
        Character[] characters = {'0', '1', '3', '5', '6', '7', '8', 'I', 'C', 'P'};
        List<Character> characters1 = Arrays.asList(characters);
        char[] chars = salvageDesc.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 || i == 4 || i == 13) {
                continue;
            }
            if (StringUtils.getLength(chars[0] + "" + chars[4] + chars[13]) != 6 || !characters1.contains(chars[i])) {
                // salvageDesc=salvageDesc;
                throw new RuntimeException("对应的银行备注不符合条件...");
            }
        }
        logger.info("salvageDesc{string}:" + salvageDesc);
        return salvageDesc;
    }

    private Date getDamageEndDate(Date damageStartDate, BigDecimal damageDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(damageStartDate);
        //calendar.add(Calendar.DAY_OF_MONTH, damageDay.intValue());
        calendar.add(Calendar.DAY_OF_MONTH, Integer.valueOf(damageDay.toString()));
        logger.info("damageEndDate{}:" + calendar.getTime());
        return calendar.getTime();
    }

    private Date getDamageStatrDate(Calendar calendar) {
        /** 获取受损时间 */
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.HOUR_OF_DAY, -2);
        calendar.add(Calendar.MINUTE, 15);
        calendar.add(Calendar.SECOND, 25);
        logger.info("damageStatrDate{}:" + calendar.getTime());
        return calendar.getTime();
    }

    private String getStatusValue(String statusCode) {
        logger.info("statusCode{}:" + statusCode);
        /** 获取状态值 */
        String statusValue = "";
        if (StringUtils.isEqualTwoStr("0", statusCode)) {
            statusValue = "已处理";
        }
        if ("1".equals(statusCode)) {
            statusValue = "处理中";
        }
        logger.info("statusValue{}:" + statusValue);
        return statusValue;
    }

    /**
     * 第一次查询时就把集合存起来
     */
    Map<String, Object> paramMap = new HashMap<>();

    private String getSalvageName(int count) {
        logger.info("count{}:" + count);
        /** 获取残值名称 */
        // Set<String> departmentNameSet=null;
        String salvageName = "";
        if (count == 0) {
            List<Department> departmentList = departmentDao.getDepartmentList();
            Set<String> departmentNameSet = departmentList.stream().map(Department::getDepartmentName).collect(Collectors.toSet());
            logger.info("departmentNameSet{}:" + JSON.toJSONString(departmentNameSet));
            if (!CollectionsUtils.isListEmpty(departmentNameSet)) {
                for (String departmentName : departmentNameSet) {
                    salvageName = departmentName;
                    departmentNameSet.remove(departmentName);
                    paramMap.put("departmentNameSet", departmentNameSet);
                    break;
                }
                logger.info("departmentNameSet{}:" + JSON.toJSONString(departmentNameSet));
                logger.info("salvageName{}:" + JSON.toJSONString(salvageName));
            }
        } else {
            Set<String> departmentNameSet1 = (Set<String>) paramMap.get("departmentNameSet");
            for (String departmentName : departmentNameSet1) {
                salvageName = departmentName;
                departmentNameSet1.remove(departmentName);
                paramMap.clear();//可以不用清除
                paramMap.put("departmentNameSet", departmentNameSet1);
                break;
            }
            logger.info("departmentNameSet{}:" + JSON.toJSONString(departmentNameSet1));
        }

        return salvageName;
    }

    private String getSeriaNo(Calendar calendar) {
        /** 添加序列号 */
        calendar.add(Calendar.MINUTE, 2);
        String dateToStr = DateUtil.DateToStr(calendar.getTime(), DateUtil.FORMATFIVE);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        logger.info("seriaNo{}:" + dateToStr + sb);
        return dateToStr + sb;
    }

    private Map<String, Object> getParamMap3(Map<String, Object> map, String riskBackCode) {
        int count = 0;
        boolean isWithOutRisk = false;
        List<RiskBack> paramList = new ArrayList<>();
        Map<String, Object> returnMap = new HashMap<>();
        String times_3 = (String) map.get("times_3");
        List<RiskBack> riskBackList2 = riskBackDao.selectThreeParams2(map);
        // List<RiskBack> riskBackList= riskBackDao.selectThreeParams(insuredIdCardList,null,null);
        Collections.sort(riskBackList2, new Comparator<RiskBack>() {
            @Override
            public int compare(RiskBack o1, RiskBack o2) {
                return o1.getStatusDesc().length() - o1.getStatusDesc().length();
            }
        });
        logger.info("riskBackList{}:" + JSON.toJSONString(riskBackList2));
        for (RiskBack riskBack : riskBackList2) {
            if (riskBackCode.equals(riskBack.getRiskBackCode())) {
                paramList.add(riskBack);
                count++;
            } else {
                count = 0;
                paramList.clear();
                continue;
            }
            if (count == Integer.valueOf(times_3)) {
                isWithOutRisk = true;
            }
        }
        returnMap.put("isWithOutRisk", isWithOutRisk);
        returnMap.put("paramList", paramList);
        return returnMap;
    }

    private void updateRiskBacklist(List<RiskBack> paramList, String riskListType, String vaildFlag) {
        for (RiskBack riskBack : paramList) {
            riskBack.setRiskListType(riskListType);
            riskBack.setValidFlag(vaildFlag);
            if (riskListType.equals("移除风险名单1")) {
                riskBack.setFailureDate(new Date());
            } else {
                riskBack.setEffectiveDate(new Date());
            }
        }
        logger.info("paramList{26}paramList的长度为:" + paramList.size());
        riskBackDao.updateRiskBacklist(paramList);
    }

    private Map<String, Object> getParamMap2(Map<String, Object> map, String riskBackCode) {
        int count = 0;
        boolean isWithOutRisk = false;
        List<RiskBack> paramList = new ArrayList<>();
        Map<String, Object> returnMap = new HashMap<>();
        String times_2 = (String) map.get("times_2");
        List<RiskBack> riskBackList2 = riskBackDao.selectThreeParams2(map);
        // List<RiskBack> riskBackList= riskBackDao.selectThreeParams(insuredIdCardList,null,null);
        Collections.sort(riskBackList2, new Comparator<RiskBack>() {
            @Override
            public int compare(RiskBack o1, RiskBack o2) {
                return o1.getStatusDesc().length() - o1.getStatusDesc().length();
            }
        });
        logger.info("riskBackList{}:" + JSON.toJSONString(riskBackList2));
        for (RiskBack riskBack : riskBackList2) {
            if (riskBackCode.equals(riskBack.getRiskBackCode())) {
                paramList.add(riskBack);
                count++;
            } else {
                count = 0;
                paramList.clear();
                continue;
            }
            if (count == Integer.valueOf(times_2)) {
                isWithOutRisk = true;
            }
        }
        returnMap.put("isWithOutRisk", isWithOutRisk);
        returnMap.put("paramList", paramList);

        return returnMap;
    }

    private List<String> getPlateNumberList(String riskBackCode) {
        return riskBackDao.getPlateNumberList(riskBackCode);
    }

    private List<String> getDriveTelList(String riskBackCode) {
        return riskBackDao.getDriveTelList(riskBackCode);
    }

    private List<String> getIdCardList(String riskBackCode) {
        return riskBackDao.getIdCardList(riskBackCode);
    }

    private void setRiskBackValue(RiskBack riskBack) {
        switch (riskBack.getRiskBackCode()) {
            case Constant.WITHOUT_RISK:
                riskBack.setRiskBackValue("无风险");
                break;
            case Constant.FALSE_LOSS:
                riskBack.setRiskBackValue("虚报损失");
                break;
            case Constant.INSURED_CHEAT:
                riskBack.setRiskBackValue("保险欺诈");
                break;
            case Constant.OTHER_RISK:
                riskBack.setRiskBackValue("其他风险");
                break;
        }
    }

    private void setInsuredMap(List<Insured> insuredList, String height, String weight) {
        for (Insured insured : insuredList) {
            Map<String, Object> map = new HashMap<>();
            map.put("height", height);
            map.put("weight", weight);
            insured.setMap(map);
        }
    }

    private String getStringJson(String conclusionCode, String insuredDesc) {
        Map<String, Object> map = new HashMap<>();
        switch (conclusionCode) {
            case Constant.CONCLUSION_TYPE_PAY:
                map.put("conclusionValue", "赔付");
                map.put("conclusionReason", "满足任何条件,符合赔付的条件");
                break;
            case Constant.CONCLUSION_TYPE_ZERO:
                map.put("conclusionValue", "零结");
                map.put("conclusionReason", "不满足任何条件,无需赔付");
                break;
            case Constant.CONCLUSION_TYPE_REJECT:
                map.put("conclusionValue", "拒赔");
                map.put("conclusionReason", "符合条件,但是已过赔付的期限");
                break;
            case Constant.CONCLUSION_TYPE_REPORT_OFF:
                map.put("conclusionValue", "报案注销");
                map.put("conclusionReason", "不符合条件,已过了时效性");
        }
        String desc = "[{\"height\":\"168cm\"},{\"weight\":\"60kg\"}]"; //[{"height":"168cm"},{"weight":"60kg"}]
        map.put("insuredDesc", insuredDesc);
        map.put("conclusionCode", conclusionCode);
        return JSON.toJSONString(map);
    }

    private String getJsonString(String jsonString) {
        Map map = JSON.parseObject(jsonString).toJavaObject(Map.class);
        String iphone2 = (String) map.get("iphone");
        map.put("iphone", StringUtils.middleSuff(iphone2, 3, 4));
        return JSON.toJSONString(map);
    }

    private void setCurreenctDesc(Policy policy) {
        /**
         * @Description: 根据币种设置币种描叙
         * @methodName: setCurreenctDesc
         * @Param: [policy]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/13 22:41
         */
        switch (policy.getCurrencyCode()) {
            case Constant.CURRENCY_CODE_CNY:
                policy.setCurrencyDesc((String) Constant.CURRENCY_CODE_MAP.get(Constant.CURRENCY_CODE_CNY));
                break;
            case Constant.CURRENCY_CODE_HKD:
                policy.setCurrencyDesc((String) Constant.CURRENCY_CODE_MAP.get(Constant.CURRENCY_CODE_HKD));
                break;
            case Constant.CURRENCY_CODE_AUD:
                policy.setCurrencyDesc((String) Constant.CURRENCY_CODE_MAP.get(Constant.CURRENCY_CODE_AUD));
                break;
            case Constant.CURRENCY_CODE_TWD:
                policy.setCurrencyDesc((String) Constant.CURRENCY_CODE_MAP.get(Constant.CURRENCY_CODE_TWD));
                break;
        }
    }

    private void setCaseValue(Policy policy) {
        /**
         * @Description: 根据案件状态获取案件状态值
         * @methodName: setCaseValue
         * @Param: [caseStatus]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/13 22:04
         */
        String caseStatus = policy.getCaseStatus();
        switch (caseStatus) {
            case Constant.CASE_STATUS_ONE:
                policy.setCaseValue((String) Constant.CASE_STATUS_MAP.get(Constant.CASE_STATUS_ONE));
                break;
            case Constant.CASE_STATUS_TWO:
                policy.setCaseValue((String) Constant.CASE_STATUS_MAP.get(Constant.CASE_STATUS_TWO));
                break;
            case Constant.CASE_STATUS_THREE:
                policy.setCaseValue((String) Constant.CASE_STATUS_MAP.get(Constant.CASE_STATUS_THREE));
                break;
            case Constant.DEPARTMENT_CODE_FOUR:
                policy.setCaseValue((String) Constant.CASE_STATUS_MAP.get(Constant.DEPARTMENT_CODE_FOUR));
                break;
        }
    }

    private String getAccidentReason(String insuredName) {
        /**
         * @Description: 根据被保险单位获取出险原因
         * @methodName: getAccidentReason
         * @Param: [insuredName]
         * @return: java.lang.String
         * @Author: scyang
         * @Date: 2020/2/13 21:45
         */
        String accidentReason = "";

        switch (insuredName) {
            case "中国建设银行":
                accidentReason = CaseReasonEnum.CASE_REASON_ONE.getNameDescList().get(0);
                break;
            case "中国农业银行":
                accidentReason = CaseReasonEnum.CASE_REASON_TWO.getNameDescList().get(0);
                break;
            case "中国工商银行":
                accidentReason = CaseReasonEnum.CASE_REASON_THREE.getNameDescList().get(0);
                break;
            case "中国招商银行":
                accidentReason = CaseReasonEnum.CASE_REASON_FOUR.getNameDescList().get(0);
                break;
        }
        return accidentReason;
    }

    private List<Department> getDepartmentListByOhter(String departmentCode) {
        /**
         * @Description: 机构代码不为2的递归查询
         * @methodName: getDepartmentListByOhter
         * @Param: [department]
         * @return: java.util.List<com.tensquare.article.pojo.Department>
         * @Author: scyang
         * @Date: 2020/2/12 22:16
         */
        List<Department> departmentList = null;
        if (departmentCode.equals(Constant.DEPARTMENT_CODE_ONE)) {
            departmentList = departmentDao.getDepartmentListByCode(departmentCode);
            for (Department department : departmentList) {
                List<Department> childrenList = departmentDao.getDepartmentListByFatherCode(departmentCode);
                department.setChildrenDepartmentList(childrenList);
            }
        } else if (departmentCode.equals(Constant.DEPARTMENT_CODE_TWO)) {
            departmentList = departmentDao.getDepartmentListByCode(departmentCode);
            for (Department department : departmentList) {
                List<Department> childrenList = departmentDao.getDepartmentListByFatherCode(departmentCode);
                department.setChildrenDepartmentList(childrenList);
            }
        } else if (departmentCode.equals(Constant.DEPARTMENT_CODE_THREE)) {
            departmentList = departmentDao.getDepartmentListByCode(departmentCode);
            for (Department department : departmentList) {
                List<Department> childrenList = departmentDao.getDepartmentListByFatherCode(departmentCode);
                department.setChildrenDepartmentList(childrenList);
            }
        }
        //departmentList=buildDepartmentList(departmentCode,departmentList);
        logger.info("departmentList{service层}:" + JSON.toJSONString(departmentList));
        return departmentList;
    }

    private List<Department> buildDepartmentList(String departmentCode, List<Department> departmentList) {
        /**
         * @Description: 递归查询
         * @methodName: buildDepartmentList
         * @Param: [department, departmentList]
         * @return: java.util.List<com.tensquare.article.pojo.Department>
         * @Author: scyang
         * @Date: 2020/2/12 22:20
         */
        List<Department> returnList = new ArrayList<>();
        /*if (!CollectionsUtils.isListEmpty(departmentList)){
            for (Department department: departmentList) {
                Department dto=new Department();
                List<Department> buildDepartmentList = buildDepartmentList(department.getDepartmentCode(), departmentList);
                dto.setDepartmentId(department.getDepartmentId());
                dto.setDepartmentCode(department.getDepartmentCode());
                dto.setDepartmentName(department.getDepartmentName());
                dto.setDepartmentLevel(department.getDepartmentLevel());
                dto.setChildrenDepartmentList(buildDepartmentList);
                returnList.add(dto);
            }
        }*/
        return returnList;
    }

    private List<Department> getDepartmentListByFour(String department) {
        /**
         * @Description: 机构代码为2的直接查询
         * @methodName: getDepartmentListByTwo
         * @Param: [department]
         * @return: java.util.List<com.tensquare.article.pojo.Department>
         * @Author: scyang
         * @Date: 2020/2/12 22:14
         */

        return departmentDao.getDepartmentListByCode(department);
    }

    private List<Department> getReturnList(List<Department> departmentList) {
        /** 一级分类 */
        List<Department> onelevelList = departmentList.stream().filter(department -> Constant.DEPARTMENT_LEVEL_ONE.equals(department.getDepartmentLevel())).collect(Collectors.toList());
        /** 二级分类 */
        List<Department> twolevelList = departmentList.stream().filter(department -> Constant.DEPARTMENT_LEVEL_TWO.equals(department.getDepartmentLevel())).collect(Collectors.toList());
        /** 三级分类 */
        List<Department> threelevelList = departmentList.stream().filter(department -> Constant.DEPARTMENT_LEVEL_THREE.equals(department.getDepartmentLevel())).collect(Collectors.toList());
        /** 四级分类 */
        List<Department> fourlevelList = departmentList.stream().filter(department -> Constant.DEPARTMENT_LEVEL_FOUR.equals(department.getDepartmentLevel())).collect(Collectors.toList());
        departmentList = getLevelList(onelevelList, twolevelList, threelevelList, fourlevelList);
        return departmentList;
    }

    private List<Department> getLevelList(List<Department> onelevelList, List<Department> twolevelList, List<Department> threelevelList, List<Department> fourlevelList) {
        List<Department> resultList = new ArrayList<>();
        for (Department oneDepartment : onelevelList) {
            List<Department> oneChildrenList = new ArrayList<>();
            for (Department twoDepartment : twolevelList) {
                List<Department> twoChildrenList = new ArrayList<>();
                if (oneDepartment.getDepartmentCode().equals(twoDepartment.getFatherCode())) {
                    oneChildrenList.add(twoDepartment);
                    oneDepartment.setChildrenDepartmentList(oneChildrenList);
                }
                for (Department threeDepartment : threelevelList) {
                    List<Department> threeChildrenList = new ArrayList<>();
                    if (twoDepartment.getDepartmentCode().equals(threeDepartment.getFatherCode())) {
                        twoChildrenList.add(threeDepartment);
                        twoDepartment.setChildrenDepartmentList(twoChildrenList);
                    }
                    for (Department fourDepartment : fourlevelList) {
                        /** 四级机构是最后一级机构没有子机构 */
                        if (threeDepartment.getDepartmentCode().equals(fourDepartment.getFatherCode())) {
                            threeChildrenList.add(fourDepartment);
                            threeDepartment.setChildrenDepartmentList(threeChildrenList);
                        }
                    }
                }
            }
            resultList.add(oneDepartment);
        }
        return resultList;
    }

    private void setDepartmentName(Department[] departmentArray) {
        /**
         * @Description: 根据机构代码设置机构名称
         * @methodName: setDepartmentName
         * @Param: [departmentArray]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/11 23:22
         */
        for (Department department : departmentArray) {
            department.setDepartmentId(idWorker.nextId() + "");
            String departmentName = "";
            switch (department.getDepartmentCode()) {
                case Constant.DEPARTMENT_CODE_ONE:
                    departmentName = (String) Constant.DEPARTMENT_CODE_MAP.get(Constant.DEPARTMENT_CODE_ONE);
                    break;
                case Constant.DEPARTMENT_CODE_TWO:
                    departmentName = (String) Constant.DEPARTMENT_CODE_MAP.get(Constant.DEPARTMENT_CODE_TWO);
                    break;
                case Constant.DEPARTMENT_CODE_THREE:
                    departmentName = (String) Constant.DEPARTMENT_CODE_MAP.get(Constant.DEPARTMENT_CODE_THREE);
                    break;
                case Constant.DEPARTMENT_CODE_FOUR:
                    departmentName = (String) Constant.DEPARTMENT_CODE_MAP.get(Constant.DEPARTMENT_CODE_FOUR);
                    break;
                default:
                    departmentName = "中国国有银行";
            }
            logger.info("departmentName{}:" + departmentName);
            department.setDepartmentName(departmentName);
        }
    }

    private List<Option> getOptionListByStatus(List<String> spection_code_list) {
        List<Option> optionList = optionDao.getOptionListByStatus(spection_code_list);
        logger.info("optionList{service层}:" + JSON.toJSONString(optionList));
        return optionList;
    }

    private List<String> getString(List<Option> optionList, int count) {
        int temp = optionList.size() > count ? count : optionList.size();
        List<Option> returnList = new ArrayList<>();
        for (int i = 0; i < temp; i++) {
            int target = new Random().nextInt(optionList.size());
            logger.info("target{}:" + target);
            returnList.add(optionList.get(target));
            optionList.remove(target);
        }
        logger.info("optionList{}:" + JSON.toJSONString(optionList));
        List<String> stringList = selectString(returnList);
        return stringList;
    }

    private List<String> selectString(List<Option> returnList) {
        List<String> list = new ArrayList<>();
        for (Option option : returnList) {
            switch (option.getOptionStatus()) {
                case OPTION_STATUS_ONE:
                    list.add("已完成");
                    break;
                case OPTION_STATUS_TWO:
                    list.add("未完成");
                    break;
            }
        }
        return list;
    }

    private void setOptionIdAndSpectionId(Spection spection, List<Option> optionList) {
        /**
         * @Description: 设置规格选项列表的主键和关联主键
         * @methodName: setOptionIdAndSpectionId
         * @Param: [spection, optionList]
         * @return: void
         * @Author: scyang
         * @Date: 2020/2/6 18:12
         */
        if (!CollectionsUtils.isListEmpty(optionList)) {
            for (Option option : optionList) {
                option.setOptionId(idWorker.nextId() + "");
                option.setSpetionId(spection.getSpectionId());
            }
            logger.info("optionList{}:" + JSON.toJSONString(optionList));
        }
    }
}
