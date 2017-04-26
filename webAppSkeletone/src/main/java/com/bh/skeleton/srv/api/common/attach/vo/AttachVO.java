package com.hs.gms.srv.api.common.attach.vo;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * AttachVO
 * 
 * @author BH Jun
 */
public class AttachVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6553745327682030519L;

    private String attachId;
    private String orgAttachId;
    private String tenantId;

    @NotEmpty
    @Pattern(regexp = "U|F|u|f")
    private String attachType;
    @NotEmpty
    @Pattern(regexp = "1|2|3|4|5|6|7")
    private String attachDataType;

    @NotEmpty
    @Length(min = 1, max = 20)
    private String procId;
    @NotEmpty
    @Length(min = 1, max = 10)
    private String procVersion;
    @NotEmpty
    @Pattern(regexp = "ko|en|ja|zh")
    private String isoLang;
    @NotEmpty
    @Length(min = 1, max = 20)
    private String tmpltId;
    @NotEmpty
    @Length(min = 1, max = 20)
    private String custAttrId;
    @NotEmpty
    @Length(min = 1, max = 20)
    private String objId;
    @NotEmpty
    @Length(min = 1, max = 20)
    private String workRequestId;
    @NotEmpty
    @Length(min = 1, max = 20)
    private String bultId;
    @NotEmpty
    @Length(min = 1, max = 20)
    private String isoIntroId;
    private int docSeq;
    @Min(value = 1)
    private int workSeq;

    private String attachName;
    private String attachContent;
    private long attachSize;
    private String filePath;

    private String[] attachNames;
    private String[] attachContents;
    private String[] tmpFileKeys;
    private String[] webEditorTmpFileKeys;

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachContent() {
        return attachContent;
    }

    public void setAttachContent(String attachContent) {
        this.attachContent = attachContent;
    }

    public long getAttachSize() {
        return attachSize;
    }

    public void setAttachSize(long attachSize) {
        this.attachSize = attachSize;
    }

    public String getOrgAttachId() {
        return orgAttachId;
    }

    public void setOrgAttachId(String orgAttachId) {
        this.orgAttachId = orgAttachId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcVersion() {
        return procVersion;
    }

    public void setProcVersion(String procVersion) {
        this.procVersion = procVersion;
    }

    public String getTmpltId() {
        return tmpltId;
    }

    public void setTmpltId(String tmpltId) {
        this.tmpltId = tmpltId;
    }

    public String getCustAttrId() {
        return custAttrId;
    }

    public void setCustAttrId(String custAttrId) {
        this.custAttrId = custAttrId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getWorkRequestId() {
        return workRequestId;
    }

    public void setWorkRequestId(String workRequestId) {
        this.workRequestId = workRequestId;
    }

    public String getBultId() {
        return bultId;
    }

    public void setBultId(String bultId) {
        this.bultId = bultId;
    }

    public String getIsoIntroId() {
        return isoIntroId;
    }

    public int getDocSeq() {
        return docSeq;
    }

    public void setDocSeq(int docSeq) {
        this.docSeq = docSeq;
    }

    public int getWorkSeq() {
        return workSeq;
    }

    public void setWorkSeq(int workSeq) {
        this.workSeq = workSeq;
    }

    public void setIsoIntroId(String isoIntroId) {
        this.isoIntroId = isoIntroId;
    }

    public String getAttachDataType() {
        return attachDataType;
    }

    public void setAttachDataType(String attachDataType) {
        this.attachDataType = attachDataType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String[] getAttachNames() {
        return attachNames;
    }

    public void setAttachNames(String[] attachNames) {
        this.attachNames = attachNames;
    }

    public String[] getAttachContents() {
        return attachContents;
    }

    public void setAttachContents(String[] attachContents) {
        this.attachContents = attachContents;
    }

    public String[] getTmpFileKeys() {
        return tmpFileKeys;
    }

    public void setTmpFileKeys(String[] tmpFileKeys) {
        this.tmpFileKeys = tmpFileKeys;
    }

    public String getIsoLang() {
        return isoLang;
    }

    public void setIsoLang(String isoLang) {
        this.isoLang = isoLang;
    }

    public String[] getWebEditorTmpFileKeys() {
        return webEditorTmpFileKeys;
    }

    public void setWebEditorTmpFileKeys(String[] webEditorTmpFileKeys) {
        this.webEditorTmpFileKeys = webEditorTmpFileKeys;
    }
}
