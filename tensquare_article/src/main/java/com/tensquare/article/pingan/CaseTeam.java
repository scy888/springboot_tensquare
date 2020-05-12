package com.tensquare.article.pingan;

import java.io.Serializable;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pingan
 * @date: 2020-05-12 20:05:04
 * @describe: 重案团队信息表
 */
public class CaseTeam implements Serializable {
    private static final long serialVersionUID = 2800921122428723379L;
    private String caseTeamId;
    private String caseTeamName;
    private String caseTeamStatus;
    private String caseTeamValue;
    private List<CasePerson> casePersonList;

    public String getCaseTeamId() {
        return caseTeamId;
    }

    public void setCaseTeamId(String caseTeamId) {
        this.caseTeamId = caseTeamId;
    }

    public String getCaseTeamName() {
        return caseTeamName;
    }

    public void setCaseTeamName(String caseTeamName) {
        this.caseTeamName = caseTeamName;
    }

    public String getCaseTeamStatus() {
        return caseTeamStatus;
    }

    public void setCaseTeamStatus(String caseTeamStatus) {
        this.caseTeamStatus = caseTeamStatus;
    }

    public String getCaseTeamValue() {
        return caseTeamValue;
    }

    public void setCaseTeamValue(String caseTeamValue) {
        this.caseTeamValue = caseTeamValue;
    }

    public List<CasePerson> getCasePersonList() {
        return casePersonList;
    }

    public void setCasePersonList(List<CasePerson> casePersonList) {
        this.casePersonList = casePersonList;
    }
}
