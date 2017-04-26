package com.hs.gms.srv.api.account.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * LoginResponsVO
 * 
 * @author BH Jun
 */
public class LoginResponseVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6047072716223474090L;

    private String tenantName;
    private String name;
    private String deptName;
    private String picURL;
    private String posName;
    private String rankName;
    private String dutyName;
    private List<String> userAuth;

    @JsonInclude(Include.NON_NULL)
    private String picPath;
    @JsonInclude(Include.NON_NULL)
    private String userId;
    @JsonInclude(Include.NON_NULL)
    private String empCode;
    @JsonInclude(Include.NON_NULL)
    private String deptId;

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public List<String> getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(List<String> userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
