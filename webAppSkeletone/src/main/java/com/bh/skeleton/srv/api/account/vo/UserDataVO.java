package com.hs.gms.srv.api.account.vo;

import java.io.Serializable;

/**
 * User Data VO.
 * 
 * @author BH Jun
 */
public class UserDataVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5703074421815058038L;

    private String userKey;
    private String userId;
    private String tenantId;
    private String tenantName;
    private String deviceType;
    private String userName;
    private String loginId;
    private String deptId;
    private String deptName;
    private String language;
    private String timeZone;
    private String empcode;
    private String[] userAuth;
    private String clientIP;
    private String issueAt;
    private String expireTime;

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String[] getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String[] userAuth) {
        this.userAuth = userAuth;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public void setUserData(LoginUserVO loginUserData) {
        this.userKey = loginUserData.getKey();
        this.userId = loginUserData.getId();
        this.userName = loginUserData.getName();
        this.empcode = loginUserData.getEmpcode();
        this.deptId = loginUserData.getDeptid();
        this.tenantId = loginUserData.getCommunityid();
        this.tenantName = loginUserData.getCommunityName();
    }

    public String getIssueAt() {
        return issueAt;
    }

    public void setIssueAt(String issueAt) {
        this.issueAt = issueAt;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

}
