package com.hs.gms.srv.api.common;

import java.io.Serializable;
import java.util.Date;

/**
 * CommonTableColumnVO
 * 
 * @author BH Jun
 */
public class CommonTableColumnVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3467578811506433919L;

    private String tenantId;
    private Date createDate;
    private String creator;
    private String creatorName;
    private String createDeptId;
    private String createDeptName;
    private Date modifyDate;
    private String modifier;
    private String modifierName;
    private String modifyDeptId;
    private String modifyDeptName;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getModifyDeptId() {
        return modifyDeptId;
    }

    public void setModifyDeptId(String modifyDeptId) {
        this.modifyDeptId = modifyDeptId;
    }

    public String getModifyDeptName() {
        return modifyDeptName;
    }

    public void setModifyDeptName(String modifyDeptName) {
        this.modifyDeptName = modifyDeptName;
    }
}
