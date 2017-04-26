package com.hs.gms.srv.api.common.attach.vo;

import java.io.Serializable;

public class AttachCustAttrVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5220367577669744158L;

    private String attachId;
    private String attachType;
    private String attachDataType;
    private String attachName;
    private String attachContent;
    private String attachSize;
    private String downloadURL;
    private String tmpFileKey;

    private String procId;
    private String procVersion;
    private String tmpltId;
    private String custAttrId;
    private String objId;
    private String custAttrName;
    private boolean favorite;

    private String dirtyFlag;

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

    public String getAttachDataType() {
        return attachDataType;
    }

    public void setAttachDataType(String attachDataType) {
        this.attachDataType = attachDataType;
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

    public String getAttachSize() {
        return attachSize;
    }

    public void setAttachSize(String attachSize) {
        this.attachSize = attachSize;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getTmpFileKey() {
        return tmpFileKey;
    }

    public void setTmpFileKey(String tmpFileKey) {
        this.tmpFileKey = tmpFileKey;
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

    public String getCustAttrName() {
        return custAttrName;
    }

    public void setCustAttrName(String custAttrName) {
        this.custAttrName = custAttrName;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getDirtyFlag() {
        return dirtyFlag;
    }

    public void setDirtyFlag(String dirtyFlag) {
        this.dirtyFlag = dirtyFlag;
    }
}
