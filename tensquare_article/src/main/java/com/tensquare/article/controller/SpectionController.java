package com.tensquare.article.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tensquare.article.jiekou.HttpRestOperations;
import com.tensquare.article.jiekou.ProvinceServince;
import com.tensquare.article.jiekou.SpectionServince;
import com.tensquare.article.pojo.*;
import com.tensquare.article.service.ProvinceServinceImpl;
import com.tensquare.article.service.SpectionServinceImpl;
import common.*;
import entity.Guarantee;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.controller
 * @date: 2020-02-06 14:03:30
 * @describe:
 */
@RestController
@CrossOrigin
//@EnableScheduling
@RequestMapping("/spection")
public class SpectionController {
    private static final Logger logge = LoggerFactory.getLogger(SpectionController.class);
    @Autowired
    private SpectionServince spectionServince;
    @Autowired
    @Qualifier("httpRest4ClaimpptJ2ee")
    private HttpRestOperations httpRestOperations;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private HttpServletRequest request;
    private static final Map<String, Method> methodMap = new HashMap<>();

    @PostMapping("/add")
    public ResponeData<Void> addSpection(@RequestBody String paramJson) {
        try {
            spectionServince.addSpection(paramJson);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, e.getMessage());
        }
    }

    @RequestMapping("/delete")
    public ResponeData<Void> deleteSpectionByIds(@RequestBody String idStr) {
        try {
            String ids = JSON.parseObject(idStr).getObject("ids", String.class);
            List<String> idList = Arrays.asList(ids.split(","));
            spectionServince.deleteSpectionByIds(idList);
            return new ResponeData<Void>(true, StatusCode.DELETESUCCESS, ResultMessage.DELETESUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.DELETEFALSE, ResultMessage.DELETESUCCESS + e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponeData<Void> updateSpection(@RequestBody String paramJson) {
        /** 传对象也可以,optionList就不能重前端获得,要new一个出来*/
        try {
            spectionServince.updateSpection(paramJson);
            return new ResponeData<Void>(true, StatusCode.UPDATESUCCESS, ResultMessage.UPDATESUCCESS);
        } catch (Exception e) {
            return new ResponeData<Void>(false, StatusCode.UPDATEFALSE, e.getMessage() + ResultMessage.UPDATEFALSE);
        }
    }

    @RequestMapping("/select")
    public ResponeData<List<Map<String, Object>>> selectAllSpection() {
        try {
            List<Map<String, Object>> mapList = spectionServince.selectAllSpection();
            //List<Map> map = JSON.parseArray(JSON.toJSONString(spectionList)).toJavaList(Map.class);
            return new ResponeData<List<Map<String, Object>>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, mapList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/findAll")
    public ResponeData<List<Spection>> findAllSpection() {
        try {
            List<Spection> spectionList = spectionServince.findAllSpection();
            return new ResponeData<List<Spection>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, spectionList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addOption")
    public ResponeData<Void> addOption(@RequestBody String paraJson) {
        try {
            spectionServince.addOption(paraJson);
            JSONObject jsonObject = JSON.parseObject(paraJson);
            logge.info("option{}:" + paraJson);
            String spectionName = jsonObject.getJSONObject("spection").getString("spectionName");
            logge.info("spectionName{}:" + spectionName);
            String option_desc_name = jsonObject.getJSONArray("optionDesc").getJSONObject(1).getString("归元寺");
            logge.info("option_desc_name{}:" + option_desc_name);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping(value = "/deleteOption", method = RequestMethod.POST)
    public ResponeData<Void> deleteOptionByIds(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            String[] ids = JSON.parseObject(paramJson).getJSONArray("ids").toJavaObject(String[].class);
            spectionServince.deleteOptionByIds(ids);
            return new ResponeData<Void>(true, StatusCode.DELETESUCCESS, ResultMessage.DELETESUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.DELETEFALSE, ResultMessage.DELETEFALSE + e.getMessage());
        }
    }

    @RequestMapping(value = "/updateOption", method = RequestMethod.POST)
    public ResponeData<Void> updateOptionBySpectionId(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            spectionServince.updateOptionBySpectionId(paramJson);
            return new ResponeData<Void>(true, StatusCode.UPDATESUCCESS, ResultMessage.UPDATESUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.UPDATEFALSE, ResultMessage.UPDATEFALSE + e.getMessage());
        }
    }

    @RequestMapping("/find")
    public ResponeData<Map<String, Object>> selectAll() {
        try {
            Map<String, Object> map = spectionServince.selectAll();
            logge.info("map{}:" + JSON.toJSONString(map));
            return new ResponeData<Map<String, Object>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, map);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/pageInfo/{pageNum}/{pageSize}")
    public ResponeData<PageInfo<Option>> pageInfo(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        try {
            PageInfo<Option> optionPageInfo = spectionServince.pageInfo(pageNum, pageSize);
            logge.info("optionPageInfo{}:" + JSON.toJSONString(optionPageInfo));
            return new ResponeData<PageInfo<Option>>(true, StatusCode.PAGESUCCESS, ResultMessage.PAGESUCCESS, optionPageInfo);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.PAGEFALSE, ResultMessage.PAGEFALSE + e.getMessage());
        }
    }

    @RequestMapping("/pageBean")
    public ResponeData<PageBean<Option>> pageBean(@RequestBody String paramJson) {
        JSONObject jsonObject = JSON.parseObject(paramJson).getJSONObject("pageBean");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        try {
            PageBean<Option> optionPageBean = spectionServince.pageBean(pageNum, pageSize);
            logge.info("optionPageBean{}:" + JSON.toJSONString(optionPageBean));
            return new ResponeData<PageBean<Option>>(true, StatusCode.PAGESUCCESS, ResultMessage.PAGESUCCESS, optionPageBean);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.PAGEFALSE, ResultMessage.PAGEFALSE + e.getMessage());
        }
    }

    @RequestMapping("/string")
    public ResponeData<Set<String>> getListString() {
        try {
            Set<String> stringSet = spectionServince.getListString();
            logge.info("{stringSet}:" + JSON.toJSONString(stringSet));
            return new ResponeData<Set<String>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, stringSet);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping(value = "/spectionDate/{spectionDate}", method = RequestMethod.GET)
    public ResponeData<List<Option>> getOptionList(@PathVariable Date spectionDate) {
        logge.info("spectionDate{}:" + JSON.toJSONString(spectionDate));
        ResponeData<List<Option>> responeData = new ResponeData<>();
        try {
            List<Option> optionList = spectionServince.getOptionList(spectionDate);
            logge.info("optionList{controller层}:" + JSON.toJSONString(optionList));
            responeData.setFlag(true);
            responeData.setCode(StatusCode.QUERYSUCCESS);
            responeData.setMsg(ResultMessage.QUERYSUCCESS);
            responeData.setData(optionList);
        } catch (Exception e) {
            responeData.setFlag(false);//Sat Feb 01 19:25:36 CST 2020
            responeData.setCode(StatusCode.QUERYSFALSE);
            responeData.setMsg(ResultMessage.QUERYSFALSE + e.getMessage());
        }
        return responeData;
    }

    public static void main(String[] args) {
        Date date = new Date(2020 - 1900, 2 - 1, 2, 9 - 14, 25, 36);
        System.out.println(date);
    }

    @RequestMapping("/addDepartment")
    public ResponeData<Void> addDepartmentList(@RequestBody String paramJson) {
        logge.info("paramJson{}:" + paramJson);
        List<Department> departmentList = JSON.parseObject(paramJson).getJSONArray("departmentList").toJavaList(Department.class);
        Department[] departmentArray = JSON.parseObject(paramJson).getJSONArray("departmentList").toJavaObject(Department[].class);
        try {
            spectionServince.addDepartmentList(departmentArray);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<Void>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/selectDrpartment")
    public ResponeData<List<Map>> getDepartmentList() {
        try {
            List<Department> departmentList = spectionServince.getDepartmentList();
            logge.info("departmentList{controller层}:" + JSON.toJSONString(departmentList) + departmentList.size());
            List<Map> mapList = JSON.parseArray(JSON.toJSONString(departmentList)).toJavaList(Map.class);
            logge.info("mapList{controller层}:" + JSON.toJSONString(mapList));
            return new ResponeData<List<Map>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, mapList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/departmentCode/{departmentCode}")
    public ResponeData<List<Department>> getDepartmentList(@PathVariable String departmentCode) {
        try {
            List<Department> departmentList = spectionServince.getDepartmentListByCode(departmentCode);
            logge.info("departmentList{controller层}:" + JSON.toJSONString(departmentList));
            return new ResponeData<List<Department>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, departmentList);
        } catch (Exception e) {
            return new ResponeData<List<Department>>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping(value = "/addPolicyList", method = RequestMethod.POST)
    public ResponeData<Void> addPolicyList(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            List<Policy> policyList = JSON.parseObject(paramJson).getJSONArray("policyList").toJavaList(Policy.class);
            spectionServince.addPolicyList(policyList);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/selectPolcyList")
    public ResponeData<List<Policy>> selectPoicyList() {
        try {
            List<Policy> policyList = spectionServince.selectPoicyList();
            logge.info("policyList{controller层}:" + JSON.toJSONString(policyList));
            return new ResponeData<List<Policy>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, policyList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/selectAll")
    public ResponeData<Map<String, Object>> getSettlendAndOutstand() {
        try {
            Map<String, Object> map = spectionServince.getSettlendAndOutstand();
            logge.info("map{}:" + JSON.toJSONString(map));
            return new ResponeData<Map<String, Object>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, map);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addGuaranteeList")
    public ResponeData<Void> addGuaranteeList(@RequestBody String paramJson) {
        try {
            List<Guarantee> guaranteeList = JSON.parseObject(paramJson).getJSONArray("guaranteeList").toJavaList(Guarantee.class);
            logge.info("guarantee{controller层}:" + JSON.toJSONString(guaranteeList));
            spectionServince.addGuaranteeList(guaranteeList);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    // @Scheduled(cron = "*/10 * * * * ?")
    @RequestMapping("/selectGuaranteeList")
    public ResponeData<List<Guarantee>> selectGuaranteeList() {
        try {
            List<Guarantee> guaranteeList = spectionServince.selectGuaranteeList();
            logge.info("guaranteeList{controller层}:" + JSON.toJSONString(guaranteeList));
            return new ResponeData<List<Guarantee>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, guaranteeList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/dateStr")
    public ResponeData<List<Guarantee>> getGuaranteeList(@RequestParam String dateStr/*@RequestBody String paramJson*/) {
        try {
          /* JSONObject jsonObject = JSON.parseObject(paramJson);
           String dateStr = jsonObject.getObject("dateStr", String.class);*/
            logge.info("dateStr{}:" + dateStr);
            String[] dateStrArray = dateStr.split(",");
            List<String> dateList = Arrays.asList(dateStrArray);
            List<Guarantee> guaranteeList = spectionServince.getGuaranteeList(dateList);
            logge.info("guaranteeList{}:", guaranteeList);
            return new ResponeData<List<Guarantee>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, guaranteeList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addInsuredList")
    public ResponeData<Void> addInsuredList(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            spectionServince.addInsuredList(paramJson);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/selectInsuredList")
    public ResponeData<List<Insured>> selectInsuredList() {
        try {
            List<Insured> insuredList = spectionServince.selectInsuredList();
            logge.info("insuredList{controller层}:" + JSON.toJSONString(insuredList));
            return new ResponeData<List<Insured>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, insuredList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addRiskBackList")
    public ResponeData<Void> addRiskBackList(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            List<RiskBack> riskBackList = JSON.parseObject(paramJson).getJSONArray("riskBackList").toJavaList(RiskBack.class);
            logge.info("riskBackList{}:" + JSON.toJSONString(riskBackList));
            spectionServince.addRiskBackList(riskBackList);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/updateRiskBackList/{riskBackCode}")
    public ResponeData<?> updateRiskBackList(@PathVariable String riskBackCode) {
        try {
            logge.info("riskBackCode{}:" + riskBackCode);
            spectionServince.updateRiskBackList(riskBackCode);
            return new ResponeData(true, StatusCode.UPDATESUCCESS, ResultMessage.UPDATESUCCESS);
        } catch (Exception e) {
            return new ResponeData(false, StatusCode.UPDATEFALSE, ResultMessage.UPDATEFALSE + e.getMessage());
        }
    }

    @RequestMapping(value = "/addSalvageList", method = RequestMethod.POST)
    public ResponeData<Void> addSalvageList(@RequestBody String paramJson) {
        try {
            List<Map> mapList = JSON.parseObject(paramJson).getJSONArray("salvageList").toJavaList(Map.class);
            logge.info("salvageList{}:" + JSON.toJSONString(mapList));
            spectionServince.addSalvageList(mapList);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/pageInfo")
    public ResponeData<PageInfo<Salvage>> getSalvagePageInfo(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            JSONObject jsonObject = JSON.parseObject(paramJson);
            Integer pageNumber = jsonObject.getInteger("pageNumber");
            Integer pageSize = jsonObject.getObject("pageSize", Integer.class);
            String statusCode = jsonObject.getString("statusCode");
            PageInfo<Salvage> salvagePageInfo = spectionServince.getSalvagePageInfo(pageNumber, pageSize, statusCode);
            return new ResponeData<PageInfo<Salvage>>(true, StatusCode.PAGESUCCESS, ResultMessage.PAGESUCCESS, salvagePageInfo);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.PAGEFALSE, ResultMessage.PAGEFALSE + e.getMessage());
        }
    }

    @Test
    public void test() throws Exception {
        Class<SpectionServinceImpl> spectionServinceClass = SpectionServinceImpl.class;
        // SpectionServince spectionServince = servinceClass.newInstance();
        for (Method method : spectionServinceClass.getDeclaredMethods()) {
            System.out.println(method);
        }
        Method method = spectionServinceClass.getDeclaredMethod("selectPoicyList");
        method.setAccessible(true);
        // method.invoke(spectionServinceClass.newInstance());
    }

    @RequestMapping("/reflect")
    public ResponeData getReflect(@RequestBody String paramJson) throws Exception {
        Class<SpectionServince> servinceClass = (Class<SpectionServince>) spectionServince.getClass();
     /* Method method = servinceClass.getDeclaredMethod("selectPoicyList",new Class[]{});
      List<Policy> policyList = (List<Policy>) method.invoke(spectionServince,new Object[]{});
      logge.info("policyList{}:"+JSON.toJSONString(policyList));*/
        JSONObject jsonObject = JSON.parseObject(paramJson);
        Map map = jsonObject.toJavaObject(Map.class);
        List<String> methodNameList = jsonObject.getJSONArray("methodNameList").toJavaList(String.class);
        methodNameList = (List<String>) map.get("methodNameList");
        if (!CollectionsUtils.isListEmpty(methodNameList)) {
            for (String methodName : methodNameList) {
                Method method = methodMap.get(methodName);
                if (method == null) {
                    method = servinceClass.getMethod(methodName, new Class[]{String.class});
                    methodMap.put(methodName, method);
                    logge.info("methodMap{}:" + JSON.toJSONString(methodMap));
                    method.invoke(spectionServince, new Object[]{/*paramJson,*/JSON.toJSONString(map)});
                }
                Object obj = Proxy.newProxyInstance(SpectionController.class.getClassLoader(), servinceClass.newInstance().getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        logge.info(method.getName() + "()  " + new Date() + "aaaaaaaaaaaaaaaaaaaaaa");
                        System.out.println("aaaaaaaaaaaaaaaaaaaaaa");
                        Object object = method.invoke(servinceClass.newInstance(), args);
                        return object;
                    }
                });
                  /*List<Spection> allSpection = spectionServince2.findAllSpection();
                  logge.info("allSpection{}:"+JSON.toJSONString(allSpection)+"aaa");*/
            }
        }
        return new ResponeData(true, StatusCode.ADDSUCCESS, "反射添加成功");
    }

    @RequestMapping("/proxy")
    public ResponeData getProxy() throws Exception {
       /* Class<SpectionServince> servinceClass = (Class<SpectionServince>) spectionServince.getClass();
        SpectionServince instance = servinceClass.newInstance();*/
        ProvinceServince provinceServince = (ProvinceServince) Proxy.newProxyInstance(SpectionController.class.getClassLoader(), new Class[]{ProvinceServince.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                logge.info(method.getName() + "()  " + new Date() + "aaa");
                logge.info("姓名: " + "盛重阳");
                return method.invoke(new ProvinceServinceImpl(), args);
            }
        });
        List<Province> servinceAll = provinceServince.findAll();
        logge.info("servinceAll{}:" + JSON.toJSONString(servinceAll) + "aaa");
        return new ResponeData(true, StatusCode.QUERYSUCCESS, "反射查询成功");
    }

    @RequestMapping("/dynamicProxy")
    public ResponeData getDynamicProxy() throws Exception {
        ProvinceServince provinceServince = new ProvinceServinceImpl();
        // InvocationHandler proxyFactory =new ProvinceServinceProxyFactory(provinceServince);
        //ProvinceServince provinceServinceProxy = proxyFactory.getProvinceServinceProxy();
        ProvinceServince provinceServinceProxy = (ProvinceServince) new DynamicProxyHandler(provinceServince).getDynamicProxyObj();
        // ProvinceServince provinceServinceProxy = (ProvinceServince) Proxy.newProxyInstance(SpectionController.class.getClassLoader(), provinceServince.getClass().getInterfaces(), proxyFactory);
        List<Province> provinceList = provinceServinceProxy.findAll();
        logge.info("provinceList{}:" + JSON.toJSONString(provinceList));
        System.out.println(provinceServinceProxy instanceof ProvinceServinceImpl);
        return new ResponeData(true, StatusCode.QUERYSUCCESS, "反射查询成功");
    }

    /*********内部类***********************************************************************************************/

    public class ProvinceServinceProxyFactory implements InvocationHandler {
        private ProvinceServince provinceServince2;

        public ProvinceServinceProxyFactory(ProvinceServince provinceServince2) {
            this.provinceServince2 = provinceServince2;
        }

        public ProvinceServince getProvinceServinceProxy() {
            ProvinceServince provinceServince3 = (ProvinceServince) Proxy.newProxyInstance(ProvinceServinceProxyFactory.class.getClassLoader(), ProvinceServinceImpl.class.getInterfaces(), this::invoke);
            return provinceServince3;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logge.info(method.getName() + "()  " + new Date());
            return method.invoke(provinceServince2, args);
        }
    }

    @RequestMapping("/addConsultationList")
    public ResponeData<List<String>> addConsultationList(@RequestBody String paramJson) {
        try {
            List<Consultation> consultationList = JSON.parseObject(paramJson).getJSONArray("consultationList").toJavaList(Consultation.class);
            logge.info("consultationList{}:" + JSON.toJSONString(consultationList));
            List<String> stringList = spectionServince.addConsultationList(consultationList);
            return new ResponeData<List<String>>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS, stringList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/selectConsultation")
    public ResponeData<List<Consultation>> selectConsultation() {
        try {
            List<Consultation> consultationList = spectionServince.selectConsultation();
            logge.info("consultationList{}:", JSON.toJSONString(consultationList));
            return new ResponeData<List<Consultation>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, consultationList);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addUserList")
    public ResponeData<Void> addUserList(@RequestBody Map<String, User[]> map) {
        try {
            User[] users = map.get("userList");
            logge.info("users{}:" + JSON.toJSONString(users));
            spectionServince.addUserList(users);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/login/{userName}/{passWord}")
    public ResponeData login(@PathVariable String userName, @PathVariable String passWord) {
        try {
            logge.info("用户名{}:" + userName + " 密码{}:" + passWord);
            User user = spectionServince.findUserName(userName);
            if (user != null && bCryptPasswordEncoder.matches(passWord, user.getPassWord())) {
                logge.info("user{}:" + JSON.toJSONString(userName));
                Map<String, Object> map = new HashMap<>();
                map.put("roles", "user");
                String token = jwtUtils.createToken(userName, JSON.toJSONString(map), "itcast", 60 * 60 * 1000L);
                logge.info("token{}:" + token);
                return new ResponeData(true, StatusCode.LOGINSUCCESS, ResultMessage.LOGINSUCCESS, token);
            } else {
                throw new ClaimpptException("用户名或密码不正确");
            }
        } catch (Exception e) {
            return new ResponeData(false, StatusCode.LOGINFALSE, ResultMessage.LOGINFALSE + " " + e.getMessage());
        }
    }

    @RequestMapping("/selectUserList")
    public ResponeData<List<User>> selectUserList() throws Exception {
        try {
            String header = request.getHeader("Header");
            if (header == null || !header.startsWith("Header ")) {
                throw new ClaimpptException("请求头不正确无法查询");
            }
            String token = header.substring(7, header.length());
            logge.info("token{}:" + token);
            Claims claims = jwtUtils.parseToken("itcast", token);
            if (claims == null) {
                throw new ClaimpptException("token为空无法查询");
            }
            String roles = (String) JSON.parseObject(claims.getSubject(), Map.class).get("roles");
            if ("user".equals(roles)) {
                List<User> userList = spectionServince.selectUserList();
                logge.info("userList{}:" + JSON.toJSONString(userList));
                return new ResponeData<List<User>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, userList);
            }
            throw new ClaimpptException("token的角色不正确无法查询");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + " " + e.getMessage());
        }
    }

    @RequestMapping("/createCode")
    public String createCode(@RequestParam String iphone) {
        String message = null;
        try {
            message = spectionServince.createCode(iphone);
            logge.info("message{}:" + message);
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }
        return message;
    }

    @RequestMapping("/register/{code}")
    public ResponeData<Void> registerUser(@RequestBody String paramJson, @PathVariable String code) {
        try {
            logge.info("paramJson{}:" + paramJson + ", code{}:" + code);
            User user = JSON.parseObject(paramJson).getJSONObject("user").toJavaObject(User.class);
            spectionServince.registerUser(user, code);
            return new ResponeData<Void>(true, StatusCode.ADDSUCCESS, "用户注册成功");
        } catch (Exception e) {
            return new ResponeData<>(false, StatusCode.ADDFALSE, "用户注册失败" + e.getMessage());
        }
    }

    @RequestMapping("/allProvince")
    public ResponeData<List<Province>> selectAllProvince() {
        try {
            List<Province> provinceList = spectionServince.selectAllProvince();
            logge.info("provinceList{}:" + JSON.toJSONString(provinceList));
            return new ResponeData<List<Province>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, provinceList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addListCity")
    public ResponeData addListCity(@RequestBody String paramJson) {
        try {
            logge.info("paramJson" + paramJson);
            List<Map> mapList = JSON.parseObject(paramJson).getJSONArray("cityList").toJavaList(Map.class);
            logge.info("mapList" + JSON.toJSONString(mapList));
            spectionServince.addListCity(mapList);
            return new ResponeData(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            return new ResponeData(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addListCity2")
    public ResponeData addListCity2(@RequestBody String paramJson) {
        try {
            logge.info("paramJson" + paramJson);
            spectionServince.addListCity2(paramJson);
            return new ResponeData(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/deleteByCityIdStr")
    public ResponeData deleteByStr(@RequestParam String ids) {
        try {
            JSON.toJSONString("ids{}:" + ids);
            List<String> asList = Arrays.asList(ids.split(","));
            spectionServince.deleteCityByIds(asList);
            return new ResponeData(true, StatusCode.DELETESUCCESS, ResultMessage.DELETESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData(false, StatusCode.DELETEFALSE, ResultMessage.DELETEFALSE);
        }
    }

    @RequestMapping("/updateByCityIds")
    public ResponeData updateCityList(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            spectionServince.updateCityList(paramJson);
            return new ResponeData(true, StatusCode.UPDATESUCCESS, ResultMessage.UPDATESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData(false, StatusCode.UPDATEFALSE, ResultMessage.UPDATEFALSE + e.getMessage());
        }
    }

    @RequestMapping("/addStudentList")
    public ResponeData addStudentList(@RequestBody Map<String, List<Student>> map) {
        try {
            // List<Student> studentList = JSON.parseObject(JSON.toJSONString(map)).getJSONArray("studentList").toJavaList(Student.class);
            List<Student> studentList = map.get("studentList");
            logge.info("studentList{}:" + JSON.toJSONString(studentList));
            spectionServince.addStudentList(studentList);
            return new ResponeData(true, StatusCode.ADDSUCCESS, ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData(false, StatusCode.ADDFALSE, ResultMessage.ADDFALSE + e.getMessage());
        }
    }

    @RequestMapping("/deleteStudentByIds")
    public ResponeData deleteStudentByIds(@RequestBody Map<String, String[]> map) {
        try {
            logge.info("map{}:" + JSON.toJSONString(map));
            String[] idArray = map.get("idArray");
            spectionServince.deleteStudentByIds(idArray);
            return new ResponeData(true, StatusCode.DELETESUCCESS, ResultMessage.DELETESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData(false, StatusCode.DELETEFALSE, ResultMessage.DELETEFALSE);
        }

    }

    @RequestMapping(value = "/selectAllStudent", method = RequestMethod.GET)
    public ResponeData<List<Student>> selectAllStudent() {
        try {
            List<Student> studentList = spectionServince.selectAllStudent();
            logge.info("studentList{}:" + JSON.toJSONString(studentList));
            return new ResponeData<List<Student>>(true, StatusCode.QUERYSUCCESS, ResultMessage.QUERYSUCCESS, studentList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData<>(false, StatusCode.QUERYSFALSE, ResultMessage.QUERYSFALSE + e.getMessage());
        }
    }

    @RequestMapping("/updateStudent")
    public ResponeData updateStudentList(@RequestBody String paramJson) {
        try {
            logge.info("paramJson{}:" + paramJson);
            List<Student> studentList = JSON.parseObject(paramJson).getJSONArray("studentList").toJavaList(Student.class);
            logge.info("studentList{}:" + JSON.toJSONString(studentList));
            spectionServince.updateStudentList(studentList);
            return new ResponeData(true, StatusCode.UPDATESUCCESS, ResultMessage.UPDATESUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData(false, StatusCode.UPDATEFALSE, ResultMessage.UPDATEFALSE + e.getMessage());
        }
    }

    @RequestMapping("/registerCode")
    public Map<String, Object> registerCode(@RequestParam String ipAddress) throws ClassCastException {
        /**
         * @Description: 注册验证码
         * @methodName: registerCode
         * @Param: [ipAdress]
         * @return: java.util.Map<java.lang.String               ,               java.lang.Object>
         * @Author: scyang
         * @Date: 2020/3/15 17:06
         */
        logge.info("验证码registerCode()方法被调用参数:" + ipAddress);
        try {
            return spectionServince.registerCode(ipAddress);
        } catch (Exception e) {
            logge.info("验证码注册报错", e);
            e.printStackTrace();
            throw new ClassCastException("验证码注册报错" + e.getMessage());
        }
    }

    @RequestMapping("/j2ee")
    public Map<String, Object> selectStudentListByJ2ee() throws ClaimpptException {
        Integer pageNum = 2;
        Integer pageSize = 2;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageBean = new HashMap<>();
        pageBean.put("pageBean", map);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);

        /*JSONObject jsonObject=new JSONObject();
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("pageSize", pageSize);
        JSONObject pageBean=new JSONObject();
        pageBean.put("pageBean", jsonObject);*/
        //Sat Feb 01 19:25:36 CST 2020
        // ResponeData responeData = httpRestOperations.getForObject("/selectAllStudent", ResponeData.class);
        //"http://localhost:9004/spection/pageBean/" + pageNum + "/" + pageSize
        //1725-08-12 00:00:00
        Date spectionDate = new Date(1358 - 1900, 5 - 1, 25);
        String depart = "2";
        ResponeData responeData2 = HttpRest4J2eeUtils.getInstance().getForObject("http://localhost:9004/spection/departmentCode/{depart}", HttpMethod.GET, ResponeData.class, depart);
        ResponeData responeData1 = HttpRest4J2eeUtils.getInstance().postForObject("http://localhost:9004/spection/pageBean", HttpMethod.POST, JSON.toJSONString(pageBean), ResponeData.class);

        logge.info("responeData2{}:" + JSON.toJSONString(responeData2));
        List<Department> departmentList = (List<Department>) responeData2.getData();
        logge.info("responeData1{}:" + JSON.toJSONString(responeData1));
        //PageBean<Option> optionPageBean = (PageBean<Option>) responeData.getData();

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("responeData1", responeData1);
        returnMap.put("departmentList", departmentList);
        return returnMap;
    }
    @RequestMapping("/weightList")
    public ResponeData<Void> addWeightLst(@RequestBody  Map<String,Object> map){
        try {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map));
            List<WeightSetting> weightList =  jsonObject.getJSONArray("weightList").toJavaList(WeightSetting.class);
           String type = (String) map.get("type");
            logge.info("weightList{}:"+JSON.toJSONString(weightList));
            logge.info("type{}:"+type);
            spectionServince.addWeightLst(weightList);
            return new ResponeData<Void>(true,StatusCode.ADDSUCCESS , ResultMessage.ADDSUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponeData<>(false,StatusCode.ADDFALSE , ResultMessage.ADDFALSE+e.getMessage());

        }
    }
}
