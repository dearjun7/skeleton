/**
 * Any software product designated as "MyWorks Proprietary Software,"
 * including computer software and may include associated media, printed
 * materials, and
 * "online" or electronic documentation ("SOFTWARE PRODUCT") is a copyrighted
 * and
 * proprietary property of MyWorks CO., LTD (“MyWorks”).
 ** The SOFTWARE PRODUCT must
 * (i) be used for MyWorks’s approved business purposes only,
 * (ii) not be contaminated by open source codes,
 * (iii) must not be used in any ways that will require it to be disclosed or
 * licensed freely to third parties or public,
 * (vi) must not be subject to reverse engineering, decompling or diassembling.
 ** MyWorks does not grant the recipient any intellectual property rights,
 * indemnities or warranties and
 * takes on no obligations regarding the SOFTWARE PRODUCT
 * except as otherwise agreed to under a separate written agreement with the
 * recipient,
 ** Revision History
 * Author Date Description
 * ------------------- ---------------- --------------------------
 * BH Jun 2016. 10. 27. First Draft
 */
package com.hs.gms.srv.api.common.filestorage.webeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.common.attach.AttachDAO;
import com.hs.gms.srv.api.common.attach.AttachUploadService;
import com.hs.gms.srv.api.common.attach.type.AttachType;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.srv.api.common.filestorage.FileStorageService;
import com.hs.gms.srv.api.common.filestorage.TempFileType;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;
import com.hs.gms.std.common.service.cache.UserCacheAccessor;
import com.hs.gms.std.common.service.http.HttpConnector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * WebEditorServiceImpl.java
 * 
 * @author BH Jun
 */
@Service
public class WebEditorServiceImpl implements WebEditorService {

    private static final String EDITOR_VIEW_NAME = "editor_cke.html";

    @Value("#{config['gms.common.charset']}")
    private String charset;

    @Autowired
    private AttachDAO attachDAO;
    @Autowired
    private AttachUploadService attachUploadService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private HttpConnector httpConnect;
    @Autowired
    private UserCacheAccessor userCacheAccessor;

    private String sendProxyURL = null;

    @PostConstruct
    public void init() {
        this.sendProxyURL = this.fileStorageService.getUploaderURL() + "/servlet/CHServlet";
    }

    @Override
    public void getWebEditor(HttpServletRequest request) throws Exception {
        String editViewURL = fileStorageService.getUploaderURL() + "/" + EDITOR_VIEW_NAME;
        fileStorageService.getRequestProxySender().executeToSendRes(request, HttpMethod.GET, editViewURL, false);
    }

    @Override
    public String createAttachDataForWebEditor(AttachVO attachVO) throws Exception {
        String[] webEditorTmpFileKeys = attachVO.getWebEditorTmpFileKeys();
        List<FileStorageUploadResultVO> uploadedFileList = attachUploadService.getTempFileDataListToCache(webEditorTmpFileKeys);
        List<Map<String, String>> convertLinkInfoList = new ArrayList<Map<String, String>>();
        String htmlPath = "";
        String webEditorAttachId = null;
        List<String> imgSrcAttachIdList = new ArrayList<String>();
        List<AttachVO> delTargetOldWebEditorFileList = attachDAO.selectAttachData(attachVO, true);

        for(FileStorageUploadResultVO tmpUploadedFileVO : uploadedFileList) {
            Map<String, String> convertLinkInfoMap = null;
            String attachName = tmpUploadedFileVO.getOrgFileName() == null || "".equals(tmpUploadedFileVO.getOrgFileName())
                    ? tmpUploadedFileVO.getFileName() : tmpUploadedFileVO.getOrgFileName();
            AttachVO insertAttachVO = new AttachVO();

            insertAttachVO = (AttachVO) SerializationUtils.clone(attachVO);

            insertAttachVO.setAttachName(attachName);
            insertAttachVO.setAttachContent(attachName);
            insertAttachVO.setFilePath(tmpUploadedFileVO.getFilePath().replace(WEB_EDITOR_STORE_ROOT_DIR, ""));
            insertAttachVO.setAttachSize(Long.parseLong(tmpUploadedFileVO.getAttachFileSize()));

            String fileId = tmpUploadedFileVO.getFileID();

            if(fileId == null || "".equals(fileId)) {
                insertAttachVO.setAttachType(AttachType.W.name());

                imgSrcAttachIdList.addAll(tmpUploadedFileVO.getImgSrcAttachIdList());
            } else {
                insertAttachVO.setAttachType(AttachType.F.name());
            }

            String attachId = attachDAO.insertAttachData(insertAttachVO);

            if(fileId != null && !"".equals(fileId)) {
                convertLinkInfoMap = new HashMap<String, String>();

                convertLinkInfoMap.put("attachId", attachId);
                convertLinkInfoMap.put("fileId", fileId);

                convertLinkInfoList.add(convertLinkInfoMap);
                imgSrcAttachIdList.add(attachId);
            } else {
                webEditorAttachId = attachId;
                htmlPath = tmpUploadedFileVO.getFilePath();
            }
        }

        if(convertLinkInfoList.size() > 0) {
            this.convertHtmlAttachedImageDownloadPath(htmlPath, convertLinkInfoList);
        }

        this.deleteWebEditorFiles(delTargetOldWebEditorFileList, imgSrcAttachIdList);
        this.removeWebEditorTempFileCache(webEditorTmpFileKeys);

        return webEditorAttachId;
    }

    private void removeWebEditorTempFileCache(String[] tmpFileKeys) throws Exception {
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        String userId = UserDataContextHolder.getUserData().getUserId();

        for(String tmpFileKey : tmpFileKeys) {
            userCacheAccessor.removeUserTmpFileInfo(tenantId, userId, tmpFileKey);
        }
    }

    @Override
    public List<String> createTempWebEditorFiles(HttpServletRequest request) throws Exception {
        String proxyResult = fileStorageService.getRequestProxySender().executeToGetRes(request, HttpMethod.POST,
                sendProxyURL + "?acton=putFile", false);
        List<FileStorageUploadResultVO> uploadedFileList = null;
        List<String> tempFileKeyList = null;

        if(fileStorageService.checkFileActionResult(proxyResult)) {
            uploadedFileList = fileStorageService.convertTempUploadResultToList(proxyResult, TempFileType.WEBEDITOR);
            tempFileKeyList = attachUploadService.setFileStorageUploadResultToTempCache(uploadedFileList);
        }

        return tempFileKeyList;
    }

    @Override
    public void getWebEditorRegisteredView(AttachVO attachVO) throws Exception {
        AttachVO webEditorFileData = attachDAO.selectAttachWebEditorHTML(attachVO);

        if(webEditorFileData == null) {
            return;
        }

        String fileSize = webEditorFileData.getAttachSize() == 0 ? null : String.valueOf(webEditorFileData.getAttachSize());

        fileStorageService.responseDownloadFile(webEditorFileData.getFilePath(), webEditorFileData.getAttachName(), null, fileSize, true);
    }

    @Override
    public void convertHtmlAttachedImageDownloadPath(String htmlPath, List<Map<String, String>> convertLinkInfoList) throws Exception {
        JSONObject convertParams = new JSONObject();

        convertParams.put("htmlPath", htmlPath);
        convertParams.put("convertLinkInfoList", JSONArray.fromObject(convertLinkInfoList));

        httpConnect.sendPut(this.sendProxyURL, convertParams, charset);
    }

    private void deleteWebEditorFiles(List<AttachVO> delTargetOldWebEditorFileList, List<String> imgSrcAttachIdList) throws Exception {
        for(AttachVO tmpVO : delTargetOldWebEditorFileList) {
            String oldAttachId = tmpVO.getAttachId();
            boolean isDeleteTarget = true;

            for(String remainedImgAttachId : imgSrcAttachIdList) {
                if(remainedImgAttachId.equals(oldAttachId)) {
                    isDeleteTarget = false;
                }
            }

            if(isDeleteTarget) {
                attachUploadService.removeAttachData(oldAttachId, tmpVO.getAttachType());
            }
        }
    }
}
