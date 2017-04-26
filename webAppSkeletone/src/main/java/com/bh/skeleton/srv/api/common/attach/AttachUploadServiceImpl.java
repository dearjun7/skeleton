package com.hs.gms.srv.api.common.attach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.common.attach.error.AttachErrorCode;
import com.hs.gms.srv.api.common.attach.type.AttachDataType;
import com.hs.gms.srv.api.common.attach.type.AttachType;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.srv.api.common.filestorage.FileStorageService;
import com.hs.gms.srv.api.common.filestorage.FileStorageType;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;
import com.hs.gms.srv.api.common.filestorage.webeditor.WebEditorService;
import com.hs.gms.srv.api.proc.ProcDAO;
import com.hs.gms.srv.api.proc.ProcVO;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.UserCacheAccessor;

import net.sf.json.JSONObject;

/**
 * AttachServiceImpl
 * 
 * @author BH Jun
 */
@Service
public class AttachUploadServiceImpl implements AttachUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachUploadServiceImpl.class);

    private static final String FILE_ATTACH_TYPE_CODE = "F";
    private static final String URL_ATTACH_TYPE_CODE = "U";
    private static final String WEB_EDITOR_TYPE_CODE = "W";
    private static final String WEB_EDITOR_IMG_DOWNLOAD_LINK_URI = "/filestorage/webeditor/download/";

    @Autowired
    private AttachDAO attachDAO;
    @Autowired
    private ProcDAO procDAO;
    @Autowired
    private UserCacheAccessor userCacheAccessor;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private WebEditorService webEditorService;

    @Override
    public boolean createAttachFile(HttpServletRequest request, AttachVO attachVO) throws Exception {
        boolean result = false;
        List<FileStorageUploadResultVO> tempFileList = fileStorageService.uploadToTemp(request); // temp 파일 업로드 수행
        result = this.createAttachFileFromUploadedTempFile(attachVO, tempFileList); // 저장 등록 경로에 저장

        return result;
    }

    @Override
    public List<FileStorageUploadResultVO> createAttachTempFile(HttpServletRequest request) throws Exception {
        List<FileStorageUploadResultVO> tempFileList = fileStorageService.uploadToTemp(request); // temp 파일 업로드 수행

        this.setFileStorageUploadResultToTempCache(tempFileList);

        return tempFileList;
    }

    @Override
    public String getAttachTempFileKey(FileStorageUploadResultVO fileStorageUploadResultVO) throws Exception {
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        String userId = UserDataContextHolder.getUserData().getUserId();
        String tmpFileKey = fileStorageUploadResultVO.getTmpFileKey();

        userCacheAccessor.setUserTmpFileInfo(tenantId, userId, tmpFileKey, fileStorageUploadResultVO);

        return tmpFileKey;
    }

    @Override
    public boolean createAttachFileFromUploadedTempFile(AttachVO attachVO) throws Exception {
        List<FileStorageUploadResultVO> uploadedTempFileList = this.getTempFileDataListToCache(attachVO.getTmpFileKeys());
        boolean result = false;

        result = this.createAttachFileFromUploadedTempFile(attachVO, uploadedTempFileList);

        return result;
    }

    @Override
    public List<FileStorageUploadResultVO> getTempFileDataListToCache(String[] tempFileKeyArr) throws Exception {
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        String userId = UserDataContextHolder.getUserData().getUserId();
        List<FileStorageUploadResultVO> uploadedTempFileList = new ArrayList<FileStorageUploadResultVO>();

        for(String tempFileKey : tempFileKeyArr) {
            JSONObject tmpFileJson = userCacheAccessor.getUserTmpFileInfo(tenantId, userId, tempFileKey);

            if(tmpFileJson == null) {
                throw new GMSException(AttachErrorCode.ATTACH_NOT_FOUND_FILE);
            }

            FileStorageUploadResultVO tmpFileInfoVO = (FileStorageUploadResultVO) JSONObject.toBean(tmpFileJson,
                    FileStorageUploadResultVO.class);

            tmpFileInfoVO.getImgSrcAttachIdList();

            uploadedTempFileList.add(tmpFileInfoVO);
        }

        return uploadedTempFileList;
    }

    private boolean createAttachFileFromUploadedTempFile(AttachVO attachVO, List<FileStorageUploadResultVO> uploadedTempFileList)
            throws Exception {

        boolean result = false;
        String saveNewDir = FileStorageType.ATTACH.getTypeDir(UserDataContextHolder.getUserData().getTenantId(), null);
        List<FileStorageUploadResultVO> moveResult = fileStorageService.moveUploadedTempFileToStorage(uploadedTempFileList, saveNewDir);
        String[] attachNameArr = attachVO.getAttachNames();
        int index = 0;

        if(attachNameArr != null ? attachNameArr.length != moveResult.size() : true) {
            attachNameArr = null;
        }

        for(FileStorageUploadResultVO tmpResultVO : moveResult) { // 업로드 결과 DB 저장
            LOGGER.debug("createUploadFile Result(Temp Upload) : " + tmpResultVO.getSysFileName());

            String attachName = null;

            if(attachNameArr != null) {
                attachName = attachNameArr[index];
            }

            this.makeAttachData(attachVO, attachName, FILE_ATTACH_TYPE_CODE, tmpResultVO);

            if(!(attachDAO.insertAttachData(attachVO) != null ? true : false)) {
                throw new GMSException(AttachErrorCode.ATTACH_FAIL);
            }

            index++;
        }

        return result;
    }

    @Override
    public boolean createAttachURL(AttachVO attachVO) throws Exception {
        boolean result = false;
        String[] attachContentsArr = attachVO.getAttachContents();
        String[] attachNameArr = attachVO.getAttachNames();

        if(attachContentsArr == null || attachContentsArr.length == 0 || attachNameArr == null
                || attachNameArr.length != attachContentsArr.length) {
            throw new GMSException(AttachErrorCode.ATTACH_BAD_REQ);
        }

        int index = 0;
        for(String attachContent : attachContentsArr) {
            this.makeAttachData(attachVO, URL_ATTACH_TYPE_CODE, attachNameArr[index], attachContent, 0, null);
            result = attachDAO.insertAttachData(attachVO) != null ? true : false;

            if(!result) {
                throw new GMSException(AttachErrorCode.ATTACH_FAIL);
            }
            index++;
        }

        return result;
    }

    @Override
    public List<String> copyAttachData(List<String> sourceAttachIdList, String destProcId, String destProcVersion) throws Exception {
        return this.copyAttachData(sourceAttachIdList, destProcId, destProcVersion, null);
    }

    @Override
    public List<String> copyAttachData(List<String> sourceAttachIdList, String destProcId, String destProcVersion, String destObjId)
            throws Exception {
        AttachVO attachVO = null;
        List<String> resultAttachIdList = new ArrayList<String>();
        List<AttachVO> sourceAttachVOList = null;

        for(String sourceAttachId : sourceAttachIdList) {
            attachVO = new AttachVO();
            attachVO.setAttachId(sourceAttachId);

            if(sourceAttachVOList == null) {
                sourceAttachVOList = attachDAO.selectAttachData(attachVO);
            } else {
                sourceAttachVOList.addAll(attachDAO.selectAttachData(attachVO));
            }
        }

        resultAttachIdList = this.copyAttachFileAndData(sourceAttachVOList, destProcId, destProcVersion);

        return resultAttachIdList;
    }

    @Override
    public void copyProcTypeAttachData(String procId, String sourceProcVersion, String destProcVersion) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.PROCESS.getAttachDataTypeCode());
        attachVO.setProcId(procId);
        attachVO.setProcVersion(sourceProcVersion);

        this.copyAttachFileAndData(attachDAO.selectAttachData(attachVO), null, destProcVersion);
    }

    @Override
    public void copyProcTypeAttachData(String sourceProcId, String sourceProcVersion, String destProcId, String destProcVersion)
            throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.PROCESS.getAttachDataTypeCode());
        attachVO.setProcId(sourceProcId);
        attachVO.setProcVersion(sourceProcVersion);

        this.copyAttachFileAndData(attachDAO.selectAttachData(attachVO), destProcId, destProcVersion);
    }

    @Override
    public void copySubObjTypeAttachData(String objId, String custAttrId, String procId, String sourceProcVersion, String destProcVersion)
            throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.SUB_OBJECT.getAttachDataTypeCode());
        attachVO.setObjId(objId);
        attachVO.setCustAttrId(custAttrId);
        attachVO.setProcId(procId);
        attachVO.setProcVersion(sourceProcVersion);

        this.copyAttachFileAndData(attachDAO.selectAttachData(attachVO), null, destProcVersion);
    }

    @Override
    public void copySubObjTypeAttachData(String sourceObjId, String sourceCustAttrId, String sourceProcId, String sourceProcVersion,
            String destObjId, String destProcId, String destProcVersion) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.SUB_OBJECT.getAttachDataTypeCode());
        attachVO.setObjId(sourceObjId);
        attachVO.setCustAttrId(sourceCustAttrId);
        attachVO.setProcId(sourceProcId);
        attachVO.setProcVersion(sourceProcVersion);

        this.copyAttachFileAndData(attachDAO.selectAttachData(attachVO), destObjId, destProcId, destProcVersion);
    }

    @Override
    public void copyISOAttachData(String sourceProcId, String sourceProcVersion, String isoLang, String destProcId, String destProcVersion)
            throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.ISO.getAttachDataTypeCode());
        attachVO.setProcId(sourceProcId);
        attachVO.setProcVersion(sourceProcVersion);
        attachVO.setIsoLang(isoLang);

        List<AttachVO> sourceAttachList = attachDAO.selectAttachData(attachVO, true);

        this.copyAttachFileAndData(sourceAttachList, null, destProcId, destProcVersion);

        attachVO.setProcVersion(destProcVersion);
        List<AttachVO> destAttachList = attachDAO.selectAttachData(attachVO, true);

        String htmlPath = null;
        List<Map<String, String>> convertLinkInfoList = new ArrayList<Map<String, String>>();

        for(AttachVO tmpAttach : destAttachList) {
            if(tmpAttach.getAttachType().equals("W")) {
                htmlPath = WebEditorService.WEB_EDITOR_STORE_ROOT_DIR + tmpAttach.getFilePath();
            } else {
                Map<String, String> convertLinkInfoMap = new HashMap<String, String>();

                convertLinkInfoMap.put("attachId", tmpAttach.getAttachId());

                String fakeFileId = WEB_EDITOR_IMG_DOWNLOAD_LINK_URI;
                for(AttachVO sourceAttach : sourceAttachList) {
                    String sourceAttachName = sourceAttach.getAttachName();
                    long sourceAttachSize = sourceAttach.getAttachSize();

                    if(sourceAttachName.equals(tmpAttach.getAttachName()) && sourceAttachSize == tmpAttach.getAttachSize()) {
                        fakeFileId = fakeFileId + sourceAttach.getAttachId();
                    }
                }
                convertLinkInfoMap.put("fileId", fakeFileId);
                convertLinkInfoList.add(convertLinkInfoMap);
            }
        }

        webEditorService.convertHtmlAttachedImageDownloadPath(htmlPath, convertLinkInfoList);
    }

    @Override
    public boolean removeAttachData(String attachId, String attachType) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setAttachId(attachId);
        attachVO.setAttachType(attachType);

        this.deleteAttachFile(attachVO);

        return attachDAO.deleteAttachData(attachId);
    }

    @Override
    public boolean removeAttachData(String tenantId, String attachId, String attachType) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(tenantId);
        attachVO.setAttachId(attachId);
        attachVO.setAttachType(attachType);

        this.deleteAttachFile(attachVO, true);

        return attachDAO.deleteAttachData(attachVO, true);
    }

    @Override
    public boolean removeProcTypeAttachData(String procId, String procVersion) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.PROCESS.getAttachDataTypeCode());
        attachVO.setProcId(procId);
        attachVO.setProcVersion(procVersion);

        this.deleteAttachFile(attachVO);

        return attachDAO.deleteAttachData(attachVO);
    }

    @Override
    public boolean removeProcTypeAttachData(String procId, String procVersion, String lang) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.ISO.getAttachDataTypeCode());
        attachVO.setProcId(procId);
        attachVO.setProcVersion(procVersion);
        attachVO.setIsoLang(lang);

        this.deleteAttachFile(attachVO);

        return attachDAO.deleteAttachData(attachVO);
    }

    @Override
    public boolean removeObjTypeAttachData(String objId) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.OBJECT.getAttachDataTypeCode());
        attachVO.setObjId(objId);

        this.deleteAttachFile(attachVO);

        return attachDAO.deleteAttachData(attachVO);
    }

    @Override
    public boolean removeSubObjTypeAttachData(String procId, String procVersion, String objId) throws Exception {
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachDataType(AttachDataType.SUB_OBJECT.getAttachDataTypeCode());
        attachVO.setProcId(procId);
        attachVO.setProcVersion(procVersion);
        attachVO.setObjId(objId);

        this.deleteAttachFile(attachVO);

        return attachDAO.deleteAttachData(attachVO);
    }

    @Override
    public boolean modifyAttachData(String attachId, String attachName, String attachContent) throws Exception {
        String tenantId = UserDataContextHolder.getUserData().getTenantId();

        return attachDAO.updateAttachData(attachId, attachName, attachContent, tenantId);
    }

    private List<String> copyAttachFileAndData(List<AttachVO> sourceAttachList, String destProcId, String destProcVersion)
            throws Exception {
        return this.copyAttachFileAndData(sourceAttachList, null, destProcId, destProcVersion);
    }

    private List<String> copyAttachFileAndData(List<AttachVO> sourceAttachList, String destObjId, String destProcId, String destProcVersion)
            throws Exception {
        List<String> resultAttachId = new ArrayList<String>();
        AttachVO copyAttachVO = null;
        String sourceFilePath = null;
        String destFilePath = null;
        String[] fileAttachTypeArr = {AttachType.F.toString(), AttachType.W.toString()};
        String copyCompleteFilePath = null;

        for(AttachVO sourceAttachVO : sourceAttachList) {
            copyAttachVO = new AttachVO();
            sourceFilePath = sourceAttachVO.getFilePath();
            destFilePath = null;
            copyCompleteFilePath = null;
            copyAttachVO = (AttachVO) SerializationUtils.clone(sourceAttachVO);

            for(String fileAttachType : fileAttachTypeArr) {
                if(fileAttachType.equals(copyAttachVO.getAttachType())) {
                    destFilePath = this.makeDestFilePath(sourceFilePath);
                    copyCompleteFilePath = fileStorageService.copyUploadedFile(sourceFilePath, destFilePath, true);

                    break;
                }
            }

            if(destProcId != null) {
                copyAttachVO.setProcId(destProcId);
            }

            if(destObjId != null) {
                copyAttachVO.setObjId(destObjId);
            }

            copyAttachVO.setOrgAttachId(this.getOrgAttachId(sourceAttachVO));
            copyAttachVO.setProcVersion(destProcVersion);
            copyAttachVO.setFilePath(copyCompleteFilePath);

            resultAttachId.add(attachDAO.insertAttachData(copyAttachVO));
        }

        return resultAttachId;
    }

    private String getOrgAttachId(AttachVO sourceAttachVO) {
        String orgAttachId = null;
        ProcVO procVO = procDAO.selectProcLastVersion(sourceAttachVO.getProcId(), false, false);
        String previousVersion = null;
        AttachVO paramVO = new AttachVO();

        if(procVO != null) {
            previousVersion = procVO.getPreviousVersion();
        }

        if(previousVersion != null && !"".equals(previousVersion)) {
            paramVO = (AttachVO) SerializationUtils.clone(sourceAttachVO);
            paramVO.setProcVersion(procVO.getLastVersion());
            paramVO.setAttachId(null);

            List<AttachVO> previousAttachList = attachDAO.selectAttachData(paramVO);

            for(AttachVO tmpAttachVO : previousAttachList) {
                if(tmpAttachVO.getAttachContent().equals(sourceAttachVO.getAttachContent())) {
                    orgAttachId = tmpAttachVO.getAttachId();
                }
            }
        }

        return orgAttachId;
    }

    private String makeDestFilePath(String sourceFilePath) {
        StringBuffer result = new StringBuffer();
        String[] tmpFilePathArr = sourceFilePath.split("/");

        for(int i = 0; i < tmpFilePathArr.length - 1; i++) {
            if(i == 0) {
                result.append(tmpFilePathArr[i]);
            } else {
                result.append("/").append(tmpFilePathArr[i]);
            }
        }

        return result.toString();
    }

    private void deleteAttachFile(AttachVO paramVO) throws Exception {
        deleteAttachFile(paramVO, false);
    }

    private void deleteAttachFile(AttachVO paramVO, boolean isTargetTenant) throws Exception {
        List<AttachVO> selectAttachList = attachDAO.selectAttachData(paramVO, true, isTargetTenant);

        for(AttachVO tmpDelAttach : selectAttachList) {
            String tmpAttachType = tmpDelAttach.getAttachType();
            if(FILE_ATTACH_TYPE_CODE.equals(tmpAttachType) || WEB_EDITOR_TYPE_CODE.equals(tmpAttachType)) {
                fileStorageService.removeUploadedFile(tmpDelAttach.getFilePath());
            }
        }
    }

    private AttachVO makeAttachData(AttachVO attachVO, String attachName, String attachType, FileStorageUploadResultVO fileVO) {
        return this.makeAttachData(attachVO, attachType, attachName, fileVO.getOrgFileName(), Long.parseLong(fileVO.getAttachFileSize()),
                fileVO.getFilePath());
    }

    private AttachVO makeAttachData(AttachVO attachVO, String attachType, String attachName, String attachContent, long attachSize,
            String physicalFilePath) {
        attachVO.setAttachType(attachType);
        attachVO.setAttachName(attachName == null ? attachContent : attachName);
        attachVO.setAttachSize(attachSize);
        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        if(URL_ATTACH_TYPE_CODE.equals(attachType)) {
            attachVO.setAttachContent(attachContent);
        } else if(FILE_ATTACH_TYPE_CODE.equals(attachType)) {
            attachVO.setAttachContent(attachContent);
            attachVO.setFilePath(!physicalFilePath.startsWith("/") ? "/" + physicalFilePath : physicalFilePath);
        }

        return attachVO;
    }

    @Override
    public List<String> setFileStorageUploadResultToTempCache(List<FileStorageUploadResultVO> tempFileList) throws Exception {
        List<String> tempFileKeyList = new ArrayList<String>();
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        String userId = UserDataContextHolder.getUserData().getUserId();

        for(FileStorageUploadResultVO tmpFileVO : tempFileList) {
            String tempFileKey = tmpFileVO.getTmpFileKey();
            userCacheAccessor.setUserTmpFileInfo(tenantId, userId, tempFileKey, tmpFileVO);

            tempFileKeyList.add(tempFileKey);
        }

        return tempFileKeyList;
    }
}
