package com.hs.gms.srv.api.account.vo;

import java.io.Serializable;

/**
 * LoginUserVO
 * 
 * @author Ma JoonChae
 */
public class LoginUserVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7596032255378906352L;

    private String id;
    private String empcode;
    private String deptid;
    private String key;
    private String name;
    private String uname;
    private String cn;
    private String isadditionalofficer;
    private String communityid;
    private String communityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getIsadditionalofficer() {
        return isadditionalofficer;
    }

    public void setIsadditionalofficer(String isadditionalofficer) {
        this.isadditionalofficer = isadditionalofficer;
    }

    public String getCommunityid() {
        return communityid;
    }

    public void setCommunityid(String communityid) {
        this.communityid = communityid;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
