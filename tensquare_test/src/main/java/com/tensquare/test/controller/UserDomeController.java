package com.tensquare.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tensquare.req.UserDtoReq;
import com.tensquare.test.annotation.AdminId;
import com.tensquare.test.annotation.AdminName;
import com.tensquare.test.dao.AdminDaoJpa;
import com.tensquare.test.dao.UserDomeDao;
import com.tensquare.test.dao.UserDomeDaoJpa;
import com.tensquare.test.dao.UserDtoDaoJpa;
import com.tensquare.test.enums.ExceptionEnum;
import com.tensquare.test.pojo.*;
import common.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.test.controller
 * @date: 2020-07-04 16:12:43
 * @describe:
 */
@RestController
@RequestMapping("/userDome")
@Slf4j
public class UserDomeController {
    @Autowired
    private UserDomeDaoJpa userDomeDaoJpa;
    @Autowired
    private JpaRepository<UserDto, Integer> jpaRepository;
    @Autowired
    private AdminDaoJpa adminDaoJpa;
    @Autowired
    private UserDomeDao userDomeDao;
    @Autowired
    private UserDtoDaoJpa userDtoDaoJpa;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private Config config;
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private JacksonUtils jacksonUtils;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private IdWorker idWorker;
    //@Autowired
    // private JavaMailSender mailSender;
    @Value("${publicKey}")
    private String publicKey;
    @Value("${privateKey}")
    private String privateKey;
    private static final String ADMIN_ID = "admin_id";
    private static final Logger logger = LoggerFactory.getLogger(UserDomeController.class);

    @PostMapping("/addList")
    public List<User> addList(@RequestBody List<User> userList) {
        LocalDate brithday = LocalDate.of(1991, 10, 16);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2020 - 1900, 6 - 1, 30, 15, 17, 26));
        for (User user : userList) {
            user.setBirthday(brithday);
            brithday = brithday.plusYears(1)
                    .minusMonths(1)
                    .plusDays(2);
            user.setCreateDate(calendar.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.add(Calendar.MINUTE, -2);
            calendar.add(Calendar.SECOND, 2);
        }
        log.info("userList:{}", userList);
        logger.info("userList:{}", JSON.toJSONString(userList));
        return userDomeDaoJpa.saveAll(userList);
    }

    @PostMapping("/select")
    public Map<String, Object> select(@RequestBody String paramJson) {
        JSONObject jsonObject = JSON.parseObject(paramJson);
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        User user = jsonObject.getJSONObject("user").toJavaObject(User.class);
        Example<User> example = Example.of(user);
        Sort sort = Sort.by(Sort.Direction.DESC, "birthday").and(Sort.by(Sort.Direction.ASC, "age"));
        // Sort.by(Sort.Order.by("birthday")).descending().and(Sort.by(Sort.Order.by("age"))).ascending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<User> userPage = userDomeDaoJpa.findAll(example, pageable);
        Map<String, Object> retunMap = new HashMap<>();
        retunMap.put("content", userPage.getContent());
        retunMap.put("totalElements", userPage.getTotalElements());
        retunMap.put("totalPages", userPage.getTotalPages());
        log.info("map:{}", retunMap);
        return retunMap;
    }

    @RequestMapping("/findOne")
    public User findOne() {
        User user = new User();
        user.setName("周芷若2");
        Example<? extends User> example = Example.of(user);
        Optional<? extends User> optionalUser = userDomeDaoJpa.findOne(example);
        optionalUser.ifPresent(e -> {
            e.setName("芷若");
            e.setAge(e.getAge() > 30 ? e.getAge() : e.getAge() + 1);
        });
        log.info("user:{}", JSON.toJSONString(optionalUser.get()));
        String dateStr = dateUtils.getDateStr(1991, 10, 16, dateUtils.getPATTERN_TWO());
        log.info("date:{}", dateStr);
        log.info("name:{},address:{},age:{}", config.getName(), config.getAddress(), config.getAge());
        return optionalUser.get();
    }

    @RequestMapping("/idList")
    public List<User> getUserList(@RequestBody List<Integer> idList) {
        log.info("idList:{}", JSON.toJSONString(idList));
        List<User> userList = userDomeDaoJpa.findByUserIdInOrderByAgeDesc(idList);
        log.info("cuserList{}:", JSON.toJSONString(userList));
        return userList;
    }

    @RequestMapping("/like/{name}")
    public List<User> getUserListLike(@PathVariable String name) throws Exception {
        name = URLDecoder.decode(name, "utf-8");
        name = name + "%";
        List<User> userList = userDomeDaoJpa.findByNameLike(name);
        /** 根据年龄从大到小排序 */
        userList = userList.stream().distinct().sorted((user1, user2) -> Integer.compare(user2.getAge(), user1.getAge())).collect(Collectors.toList());
        log.info("cuserList{}:", JSON.toJSONString(userList));
        return userList;
    }

    @GetMapping("/greaterThanEqual/{age}")
    public List<User> findByAgeGreaterThanEqualOrderByBirthday(@PathVariable int age) {
        List<User> userList = userDomeDaoJpa.findByAgeGreaterThanEqualOrderByBirthdayAsc(age);
        /** 根据姓名和年龄排序 */
        Map<String, List<User>> listMap = userList.stream().distinct().collect(Collectors.groupingBy(user -> user.getName() + ":" + user.getAge()));
        log.info("listMap:{}" + listMap);
        return userList;
    }

    @GetMapping("/ageOrName/{age}/{name}")
    public List<User> ageOrNameUserList(@PathVariable int age, @PathVariable String name) {
        List<User> userList = userDomeDaoJpa.findByAgeOrName(age, name);
        log.info("userList:{}" + userList);
        /** 根据姓名和年龄去重 */
        userList = userList.stream().distinct()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(user -> user.getName() + ":" + user.getAge()))),
                        ArrayList::new));
        log.info("userList:{}" + userList);
        return userList;
    }

    @RequestMapping("/update/{id}")
    public User update(@PathVariable int id) {
        Optional<User> optional = userDomeDaoJpa.findById(id);
        if (optional.isPresent()) {
            log.info("修改前的user:{}", optional.get());
            log.info("name:{},address:{},age:{}", config.getName(), config.getAddress(), config.getAge());
            User user = optional.get();
            user = userDomeDaoJpa.save(user.setName(user.getName() + "2").setAddress(user.getAddress() + "2"));
            log.info("修改后的user:{}", user);
            return user;
        }
      /*  optional.ifPresent(user->{
            user.setName(user.getName()+"2");
            user.setAddress(user.getAddress()+"2");

        });*/
        return null;
    }

    @GetMapping("/updateBy/{id}/{address}")
    //@Transactional
    public void updateBy(@PathVariable int id, @PathVariable String address) {
        userDomeDaoJpa.updateBy(id, address);
    }

    @GetMapping("/selectBy/{address}/{sex}")
    public List<User> selectBy(@PathVariable String address, @PathVariable String sex) {
        List<User> userList = userDomeDaoJpa.selectBy(address, sex);
        log.info("userList:{}", JSON.toJSONString(userList));
        return userList;
    }

    @PostMapping("/saveOne")
    public int saveOne(@RequestBody String parmJson) {
        User user = JacksonUtils.toObject(parmJson, User.class);
        user.setBirthday(LocalDate.of(1991, 9, 9));
        log.info("user:{}", JacksonUtils.toString(user));
        return userDomeDao.saveOne(user);
    }

    @PostMapping("/addOne")
    public User addOne(@RequestBody User user) {
        log.info("user:{}", new JacksonUtils().toString(user));
        return userDomeDaoJpa.save(user);
    }

    @PostMapping("/saveList")
    public List<User> saveList(@RequestBody String paramJson) {
        List<User> userList = JacksonUtils.toList(paramJson, new TypeReference<List<User>>() {
        });
        LocalDate localDate = LocalDate.of(1991, 10, 16);
        for (User user : userList) {
            localDate = localDate.minusYears(1)
                    .plusMonths(2)
                    .plusDays(2);
            user.setBirthday(localDate);
        }
        log.info("userList:{}", jacksonUtils.toString(userList));
        userList = userDomeDao.saveList(userList);
        log.info("userList2:{}", jacksonUtils.toString(userList));
        return userList;
    }

    @GetMapping("/findBySex")
    public List<User> getList(@RequestParam String sex) {
        List<User> userList = userDomeDao.getList(sex);
        log.info("userList:{}", jacksonUtils.toString(userList));
        return userList;
    }

    @PostMapping("/addSome")
    public List<User> addSome(@RequestBody String paramJson) {
        List<User> userList = JSON.parseObject(paramJson).getJSONArray("userList").toJavaList(User.class);
        LocalDate birthday = LocalDate.of(2020, 7, 14);
        for (User user : userList) {
            user.setBirthday(birthday);
            birthday = birthday.minusYears(1)
                    .plusMonths(2)
                    .plusDays(3);
        }
        logger.info("保存前,userList:{}", JSON.toJSONString(userList));
        userList = userDomeDaoJpa.saveAll(userList);
        logger.info("保存后,userList:{}", JSON.toJSONString(userList));
        userList = userDomeDao.findUserByNameAndSex(userList);
        logger.info("保存后,userList:{}", JSON.toJSONString(userList));
        return userList;
    }

    @GetMapping("/maxBirthday/{name}")
    public LocalDate maxBirthday(@PathVariable String name) {
        LocalDate birthday = userDomeDaoJpa.maxBirthday(name);
        log.info("出生日期最大值：{}", birthday);
        return birthday;
    }

    @PostMapping("/addUserDtoList")
    public List<UserDto> addUserDtoList(@RequestBody List<UserDto> userDtoList) {
        log.info("添加前的userDtoList:{}", jacksonUtils.toString(userDtoList));
        userDtoList = jpaRepository.saveAll(userDtoList);
        log.info("添加后的userDtoList:{}", jacksonUtils.toString(userDtoList));
        String head = request.getHeader("head");
        log.info("head:{}", head);
        return userDtoList;
    }

    @PostMapping("/addUserDto")
    public Result addUserDto(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws Exception {
        log.info("添加前的userDtoList:{}", jacksonUtils.toString(userDto));
        String mesg = "";
        Result result = null;
        for (ObjectError error : bindingResult.getAllErrors()) {
            mesg = mesg + "/" + error.getDefaultMessage();
        }
        log.info("mesg:{}", jacksonUtils.toString(mesg));
        if (!StringUtils.isEmpyStr(mesg)) {
            result = new Result().setResultCode(ExceptionEnum.PARAMS_ERROR.getRetCode())
                    .setResultMessage(ExceptionEnum.PARAMS_ERROR.getRetMessage());
        }
        String context = "{\"name\":\"张无忌\",\"address\":\"明教\",\"age\":20,\"sex\":\"男\"}";
        //rsa加密
        String encrypt = RsaUtil.encrypt(context, publicKey);
        log.info("context加密：{}", encrypt);
        //rsa解密
        String decrypt = RsaUtil.decrypt(encrypt, privateKey);
        log.info("context解密：{}", decrypt);
        userDto.setContext(decrypt);
        //加签
        String generateSign = RsaUtil.generateSign(context, privateKey, "SHA256withRSA");
        log.info("context加签：{}", generateSign);
        boolean verify = RsaUtil.verifyWithMd5(context, generateSign, publicKey, "SHA256withRSA");
        if (!verify) {
            result = new Result().setResultCode(ExceptionEnum.SIGN_ERROR.getRetCode())
                    .setResultMessage(ExceptionEnum.SIGN_ERROR.getRetMessage());
        } else {
            result = new Result().setResultCode(ExceptionEnum.SUCCESS.getRetCode())
                    .setResultMessage(ExceptionEnum.SUCCESS.getRetMessage());
        }
        // userDto = jpaRepository.save(userDto);
        log.info("userDto:{}", jacksonUtils.toString(userDto));
        String head = request.getHeader("head");
        log.info("head:{}", head);
        return result;
    }

    @RequestMapping("/addAdmin")
    public Admin addAdmin(@RequestBody @Valid AdminReq adminReq, BindingResult bindingResult) throws Exception {
        log.info("adminReq:{}", jacksonUtils.writeValueAsString(adminReq));
        /** 检验入参 */
        String errorMsg = "";
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorMsg += "/" + error;
        }
        if (!StringUtils.isEmpyStr(errorMsg)) {
            log.info("errorMsg:{}", errorMsg);
            throw new RuntimeException("参数校验不符合规定...");
        }
        Admin admin = new Admin();
        admin.setAdminId(idWorker.nextId() + "")
                .setAdmin(adminReq.getAdmin())
                .setPassword(encoder.encode(adminReq.getPassword()))
                .setAuthCode(adminReq.getAuthCode())
                .setIsEffetive(true);
        /** 将adminId存入session中 */
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60);
        session.setAttribute(ADMIN_ID, admin.getAdminId());
        log.info("session保存的值为:{}", session.getAttribute(ADMIN_ID));
        log.info("保存前admin:{}", jacksonUtils.writeValueAsString(admin));
        admin = adminDaoJpa.save(admin);
        log.info("保存后admin:{}", jacksonUtils.writeValueAsString(admin));
        return admin;
    }

    @RequestMapping("/login/{admin}/{password}")
    public String login(@PathVariable String admin, @PathVariable String password) {
        String loginMsg = "";
        log.info("用户登录名admin:{},用户登录密码password:{}", admin, password);
        Admin ad = adminDaoJpa.findByAdmin(admin);
        log.info("用户信息Admin：{}", ad);
        boolean isFlag = encoder.matches(password, ad.getPassword());
        if (isFlag) {
            loginMsg = "用户名密码正确,登录成功!!!";
        } else {
            loginMsg = "用户名或密码不正确,登录失败!!!";
        }
        return loginMsg;
    }
    @PostMapping("/addAdminId")
    public String addAdminId(@RequestBody Admin admin){
         String msg="";
        Admin admin2 = adminDaoJpa.findOne(Example.of(new Admin().setAdmin(admin.getAdmin()))).orElseGet(() -> {
            Admin e = new Admin();
            e.setAdminId(idWorker.nextId() + "");
            e.setAdmin(admin.getAdmin());
            e.setPassword(SecurityUtil.encoder(admin.getPassword(), "MD5"));
            e.setIsEffetive(true);
            e.setAuthCode("SCY2");
            return e;
        });
        log.info("admin2:{}",JSON.toJSONString(admin2));
        try {
            Admin save = adminDaoJpa.save(admin2);
            msg="添加admin成功:"+JSON.toJSONString(save);
        } catch (Exception e) {
            e.printStackTrace();
            msg="添加admin失败:"+e.getMessage();
        }
        log.info("msg:{}",msg);
        return msg;
    }
    @GetMapping("/login2/{admin}/{password}")
    public String login2(@PathVariable String admin,@PathVariable String password) throws Exception {
        String msg="";
        boolean exists = adminDaoJpa.exists(Example.of(new Admin().setAdmin(admin)
                .setPassword(SecurityUtil.encoder(password, "MD5"))));
        HttpSession session=null;
        if (exists) {
            session = request.getSession();
            session.setMaxInactiveInterval(60);
            session.setAttribute("ADMIN_ID", admin);
            msg="登陆成功!";
        }
        else {
            msg= "登录失败!";
        }
        log.info("msg:{}, session:{}",msg,session);
        return JSON.toJSONString(msg);
    }
    @RequestMapping("/annotation2")
    public String getString2(@AdminId String adminId) {
        log.info("参数adminId：{}", adminId);
        return adminId;
    }
    @RequestMapping("/annotation")
    public String getString(@AdminName String adminName) {
        log.info("参数adminName：{}", adminName);
        return adminName;
    }

    @PostMapping("/addUsers")
    public List<UserDto> addUsers(@RequestBody List<UserDto> userDtoList) {
        userDtoList = userDtoList.stream().map(userDto -> {
            userDto.setCreateDate(LocalDateTime.now());
            return userDto;
        }).collect(Collectors.toList());
        log.info("待添加的userDtoList：{}", JSON.toJSONString(userDtoList));
        /** 批量根据姓名和年龄查询 */
        List<UserDto> existList = userDomeDao.selectUserByNameAndAge(userDtoList);
        log.info("根据姓名和年龄批量查询existList：{}", JSON.toJSONString(existList));
        List<UserDto> addList = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            if (existList.stream().noneMatch(e -> e.getName().equals(userDto.getName())
                    && e.getName().equals(userDto.getName()))) {
                /**  如果不存在就批量添加 */
                addList.add(userDto);
            }
            /** 存在就更新 */
            else {
                try {
                    userDtoDaoJpa.updateByNameAndAge(userDto.getContext(), userDto.getSex(), userDto.getName(), userDto.getAge());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!CollectionsUtils.isListEmpty(addList)) {
            int i = userDomeDao.addList(addList);
            log.info("添加影响的行数i:{}", i);
        }
        return addList;
    }

    @PostMapping("saveOrUpadateUsers")
    public int saveOrUpadateUsers(@RequestBody String userList) {
        List<UserDto> userDtoList = jacksonUtils.toList(userList, new TypeReference<List<UserDto>>() {
        });
        userDtoList = userDtoList.stream().map(userDto -> {
            userDto.setCreateDate(LocalDateTime.now());
            return userDto;
        }).collect(Collectors.toList());
        log.info("待添加的userDtoList：{}", JSON.toJSONString(userDtoList));
        int i = userDomeDao.saveOrUpadateUsers(userDtoList);
        log.info("新增或修改影响的条数：{}", i);
        return i;
    }

    //    @GetMapping("/send")
//    public String sendEmail() throws Exception{
//        String templateContent = FreemarkerUtil.getTemplateContent("/ftl/batch-job-failure.html");
//        Map<String, Object> root = new HashMap<>();
//        root.put("operator", "operator");
//        root.put("subject", "标题");
//        root.put("subject", "Job异常信息明细");
//        root.put("jobName", "jobName");
//        root.put("jobStartTime", "jobStartTime");
//        root.put("jobEndTime", "jobStartTime");
//        root.put("jobStatus", "jobStatus");
//        root.put("jobExitCode", "jobExitCode");
//        root.put("jobExitDescription", "jobExitDescription");
//        root.put("jobIsRuning", "jobIsRuning");
//        root.put("joblastUpdateTime", "joblastUpdateTime");
//        root.put("jobDueTime", "jobDueTime");
//        root.put("stepName", "stepName");
//        root.put("stepStartTime", "stepStartTime");
//        root.put("stepEndTime", "stepEndTime");
//        root.put("stepStatus", "stepStatus");
//        root.put("stepCommitCount", "stepCommitCount");
//        root.put("stepReadCount", "stepReadCount");
//        root.put("stepWriteCount", "stepWriteCount");
//        root.put("stepExitCode", "stepExitCode");
//        root.put("stepExitDescription", "stepExitDescription");
//
//        String outputContent = FreemarkerUtil.parse(templateContent, root);
//
//        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage);
//        try {
//            mimeMessageHelper.setFrom("um-sit@weshareholdings.com.cn");
//            mimeMessageHelper.setTo(new String[]{"v_tianwenkai@weshareholdings.com",
//                    "v_shengchongyang@weshareholdings.com",
//                    "knight.song@weshareholdings.com",
//                    "jiancai.zhou@weshareholdings.com",
//                    "jiayuan.qu@weshareholdings.com",});
//            mimeMessageHelper.setSubject("Job异常信息明细");
//            mimeMessageHelper.setText(outputContent, true);
//            mailSender.send(mimeMailMessage);
//            log.info("邮件发送成功：{}", "");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            log.error("邮件发送失败：{}", "");
//        }
//        return null;
//    }
    @RequestMapping("/dynamic")
    public List<UserDto> getUserDtoList(@RequestBody Map<String, Object> map) {
        log.info("前端传过来的参数,map:{}", map);
        Integer pageNum = (Integer) map.get("pageNum");
        Integer pageSize = (Integer) map.get("pageSize");

        List<Predicate> predicateList = new ArrayList<>();
        Specification<UserDto> specification = new Specification<UserDto>() {
            @Override
            public Predicate toPredicate(Root<UserDto> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (!StringUtils.isEmpyStr((String) map.get("name"))) {
                    predicateList.add(criteriaBuilder.equal(root.get("name").as(String.class), (String) map.get("name")));
                }
                if (!StringUtils.isEmpyStr((String) map.get("context"))) {
                    predicateList.add(criteriaBuilder.like(root.get("context").as(String.class), (String) map.get("context") + "%"));
                }
                if ((Integer) map.get("age") != null) {

                    predicateList.add(criteriaBuilder.ge(root.get("age").as(Integer.class), (Integer) map.get("age")));
                }
                return criteriaBuilder.or(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<UserDto> userDtoPage = userDtoDaoJpa.findAll(specification,
                PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "age")
                        .and(Sort.by(Sort.Direction.ASC, "userId"))));
        log.info("userDtoPage{}:", JSON.toJSONString(userDtoPage));
        return userDtoPage.getContent();
    }

    @RequestMapping("/select2")
    public List<UserDtoReq> select2(@RequestBody UserDtoReq userDtoReq) {
        log.info("入参userDto:{}", JSON.toJSONString(userDtoReq));
        List<UserDto> userDtoList = userDtoDaoJpa.findAll(Example.of(JSON.parseObject(JSON.toJSONString(userDtoReq)).toJavaObject(UserDto.class)));
        List<UserDtoReq> userDtoReqList = userDtoList.stream().map(userDto1 -> {
            return JSON.parseObject(JSON.toJSONString(userDto1), UserDtoReq.class);
        }).collect(Collectors.toList());
        log.info("userDtoList转换=>userDtoReqList:{}", JSON.toJSONString(userDtoReqList));
        return userDtoReqList;
    }

    @RequestMapping("/updateUserDto/{name}/{age}")
    public List<UserDtoReq> updateUserDto(@PathVariable String name,@PathVariable int age) {
        List<UserDto> userDtoList = userDtoDaoJpa.findByName(name);
        log.info("姓名：{},修改前的年龄：age:{}",userDtoList.get(0).getName(),userDtoList.get(0).getAge());
        userDtoDaoJpa.updateUserDto(name,age);
         userDtoList = userDtoDaoJpa.findByName(name);
        log.info("姓名：{},修改后的年龄：age:{},",userDtoList.get(0).getName(),userDtoList.get(0).getAge());
        return userDtoList.stream().map(userDto -> {
            UserDtoReq userDtoReq=new UserDtoReq();
            BeanUtils.copyProperties(userDto, userDtoReq);
            return userDtoReq;
        }).collect(Collectors.toList());
    }
}
