package com.tensquare.article.jiekou;

import com.github.pagehelper.PageInfo;
import com.tensquare.article.pingan.*;
import com.tensquare.article.pojo.*;
import common.ClaimpptException;
import common.PageBean;
import entity.Guarantee;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.jiekou
 * @date: 2020-02-06 14:05:38
 * @describe:
 */
public interface SpectionServince {
    void addSpection(String paramJson);

    void deleteSpectionByIds(List<String> idList);

    void updateSpection(String paramJson);

    List<Map<String,Object>> selectAllSpection();

    List<Spection> findAllSpection();

    void addOption(String paramJson);

    void deleteOptionByIds( String[] ids);

    void updateOptionBySpectionId(String paramJson);

    Map<String, Object> selectAll();

    PageInfo<Option> pageInfo(Integer pageNum, Integer pageSize);

    PageBean<Option> pageBean(Integer pageNum, Integer pageSize) throws ExecutionException, InterruptedException;

    Set<String> getListString();

    List<Option> getOptionList(Date spectionDate);

    void addDepartmentList(Department[] departmentArray);

    List<Department> getDepartmentList();

    List<Department> getDepartmentListByCode(String departmentCode);

    void addPolicyList(List<Policy> policyList);

    List<Policy> selectPoicyList();

    Map<String, Object> getSettlendAndOutstand();

    void addGuaranteeList( List<Guarantee> guaranteeList);

    List<Guarantee> selectGuaranteeList();

    void addInsuredList(String paramJson) throws Exception;

    List<Insured> selectInsuredList();

    void addRiskBackList(List<RiskBack> riskBackList);

    void updateRiskBackList(String riskBackCode);

    void addSalvageList(List<Map> mapList);

    PageInfo<Salvage> getSalvagePageInfo(Integer pageNumber, Integer pageSize, String statusCode);


    List<Guarantee> getGuaranteeList(List<String> dateList);

    List<String> addConsultationList(List<Consultation> consultationList);

    List<Consultation> selectConsultation();

    void addUserList(User[] users);

    User findUserName(String userName);

    List<User> selectUserList();

    String createCode(String iphone);

    void registerUser(User user, String code) throws ClaimpptException;

    List<Province> selectAllProvince();

    void addListCity(List<Map> mapList);

    void addListCity2(String paramJson);

    void deleteCityByIds(List<String> asList);

    void updateCityList(String paramJson);

    void addStudentList(List<Student> studentList);

    void deleteStudentByIds(String[] idArray);

    List<Student> selectAllStudent();

    void updateStudentList(List<Student> studentList);

    Map<String, Object> registerCode(String ipAddress) throws Exception;


    void addWeightLst(List<WeightSetting> weightList);

    void addListCase(List<PreCase> caseList);

    void addOutBreakList(List<OutBreak> outBreakList);

    List<OutBreak> selectOutBreak();

    List<PaymentItem> generatePayList(Settlenment settlenment, List<CoinsShare> coinsShareList);

    void createNotice(MsgNotice msgNotice) throws ClaimpptException;

    void updateNotice(List<String> idsList,String status);

    void addEmploverList(List<Employer> emploverList);

    List<Employer> selectEmplover();

    void addImageList(List<Image> imageList);
}
