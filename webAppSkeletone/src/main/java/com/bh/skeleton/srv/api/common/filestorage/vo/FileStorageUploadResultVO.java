package com.hs.gms.srv.api.common.filestorage.vo;

import java.io.Serializable;
import java.util.List;

/**
 * FileStorageUploadResultVO
 * 
 * @author BH Jun
 */
public class FileStorageUploadResultVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3682935818001536592L;

    private String orgFileName;
    private String attachFileSize;
    private String attachURI;
    private String tmpFileKey;

    private String sysFileName;
    private String filePath;
    private String fileID;
    private String fileName;
    private String fileType;

    private List<String> imgSrcAttachIdList = null;

    public String getOrgFileName() {
        return orgFileName;
    }

    public void setOrgFileName(String orgFileName) {
        this.orgFileName = orgFileName;
    }

    public String getSysFileName() {
        return sysFileName;
    }

    public void setSysFileName(String sysFileName) {
        this.sysFileName = sysFileName;
    }

    public String getAttachFileSize() {
        return attachFileSize;
    }

    public void setAttachFileSize(String attachFileSize) {
        this.attachFileSize = attachFileSize;
    }

    public String getAttachURI() {
        return attachURI;
    }

    public void setAttachURI(String attachURI) {
        this.attachURI = attachURI;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTmpFileKey() {
        return tmpFileKey;
    }

    public void setTmpFileKey(String tmpFileKey) {
        this.tmpFileKey = tmpFileKey;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<String> getImgSrcAttachIdList() {
        return imgSrcAttachIdList;
    }

    public void setImgSrcAttachIdList(List<String> imgSrcAttachIdList) {
        this.imgSrcAttachIdList = imgSrcAttachIdList;
    }
}
