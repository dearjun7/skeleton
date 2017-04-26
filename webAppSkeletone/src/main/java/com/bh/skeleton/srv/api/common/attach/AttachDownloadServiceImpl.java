package com.hs.gms.srv.api.common.attach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.common.attach.error.AttachErrorCode;
import com.hs.gms.srv.api.common.attach.type.AttachDataType;
import com.hs.gms.srv.api.common.attach.vo.AttachStorageDownloadInfoVO;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.srv.api.common.filestorage.FileStorageService;
import com.hs.gms.srv.api.common.filestorage.vo.CompressSourceFileVO;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.UserCacheAccessor;

import net.sf.json.JSONObject;

/**
 * AttachDownloadServiceImpl
 * 
 * @author BH Jun
 */
@Service
public class AttachDownloadServiceImpl implements AttachDownloadService {

    private static final String FILE_INFO_PATH_KEY = "filePath";
    private static final String FILE_INFO_NAME_KEY = "fileName";
    private static final String FILE_INFO_SIZE_KEY = "fileSize";

    @Value("#{config['gms.common.charset']}")
    private String charset;

    @Autowired
    private AttachDAO attachDAO;

    @Autowired
    private UserCacheAccessor userCacheAccessor;
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public void setApiDomain(String apiDomain) {
        attachDAO.setApiDomain(apiDomain);
    }

    @Override
    public void getAttachFileDownload(String attachId) throws Exception {
        Map<String, String> fileInfo = this.getFileInfo(attachId);

        String filePath = fileInfo.get(FILE_INFO_PATH_KEY);
        String fileName = fileInfo.get(FILE_INFO_NAME_KEY);
        String fileSize = fileInfo.get(FILE_INFO_SIZE_KEY);

        fileStorageService.responseDownloadFile(filePath, fileName, fileSize);
    }

    @Override
    public void getCompressedAttachFileDownload(List<String> attachIdList) throws Exception {
        List<CompressSourceFileVO> compSourceFileList = new ArrayList<CompressSourceFileVO>();
        CompressSourceFileVO tmpSourceFileVO = null;

        for(String attachId : attachIdList) {
            Map<String, String> fileInfo = this.getFileInfo(attachId);
            tmpSourceFileVO = new CompressSourceFileVO();

            tmpSourceFileVO.setSourceFileName(fileInfo.get(FILE_INFO_NAME_KEY));
            tmpSourceFileVO.setSourceFilePath(fileInfo.get(FILE_INFO_PATH_KEY));

            compSourceFileList.add(tmpSourceFileVO);
        }

        fileStorageService.responseCompressDownloadFile(compSourceFileList);
    }

    @Override
    public void getAttachTempFileDownload(String tmpFileKey) throws Exception {
        String tenantId = UserDataContextHolder.getUserData().getTenantId();
        String userId = UserDataContextHolder.getUserData().getUserId();

        JSONObject tmpFileJson = userCacheAccessor.getUserTmpFileInfo(tenantId, userId, tmpFileKey);

        if(tmpFileJson == null) {
            throw new GMSException(AttachErrorCode.ATTACH_NOT_FOUND_FILE);
        }

        FileStorageUploadResultVO tmpFileInfo = (FileStorageUploadResultVO) JSONObject.toBean(tmpFileJson, FileStorageUploadResultVO.class);

        String filePath = tmpFileInfo.getFilePath();
        String fileName = tmpFileInfo.getOrgFileName();
        String fileSize = tmpFileInfo.getAttachFileSize();

        fileStorageService.responseDownloadFile(filePath, fileName, fileSize);
    }

    @Override
    public List<AttachStorageDownloadInfoVO> getProcTypeDownloadInfoList(String procId, String procVersion, String tmpltId,
            String custAttrId) throws Exception {
        AttachVO param = new AttachVO();

        param.setAttachDataType("1");
        param.setProcId(procId);
        param.setProcVersion(procVersion);
        param.setTmpltId(tmpltId);
        param.setCustAttrId(custAttrId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return attachDAO.selectAttachDownloadInfoList(param);
    }

    @Override
    public List<AttachStorageDownloadInfoVO> getObjectTypeDownloadInfoList(String objId, String tmpltId, String custAttrId)
            throws Exception {
        AttachVO param = new AttachVO();

        param.setAttachDataType("2");
        param.setObjId(objId);
        param.setTmpltId(tmpltId);
        param.setCustAttrId(custAttrId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return attachDAO.selectAttachDownloadInfoList(param);
    }

    @Override
    public List<AttachStorageDownloadInfoVO> getSubObjectTypeDownloadInfoList(String procId, String procVersion, String objId,
            String tmpltId, String custAttrId) throws Exception {
        AttachVO param = new AttachVO();

        param.setAttachDataType("3");
        param.setProcId(procId);
        param.setProcVersion(procVersion);
        param.setObjId(objId);
        param.setTmpltId(tmpltId);
        param.setCustAttrId(custAttrId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return attachDAO.selectAttachDownloadInfoList(param);
    }

    @Override
    public List<AttachStorageDownloadInfoVO> getChangeRequestTypeDownloadInfoList(String workRequestId) throws Exception {
        AttachVO param = new AttachVO();

        param.setAttachDataType("4");
        param.setWorkRequestId(workRequestId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return attachDAO.selectAttachDownloadInfoList(param);
    }

    @Override
    public List<AttachStorageDownloadInfoVO> getBultTypeDownloadInfoList(String bultId) throws Exception {
        AttachVO param = new AttachVO();

        param.setAttachDataType("5");
        param.setBultId(bultId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return attachDAO.selectAttachDownloadInfoList(param);
    }

    private Map<String, String> getFileInfo(String attachId) {
        Map<String, String> result = new HashMap<String, String>();
        AttachVO attachVO = new AttachVO();

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        attachVO.setAttachId(attachId);
        attachVO.setAttachType("F");

        List<AttachVO> selectResultList = attachDAO.selectAttachData(attachVO);

        if(selectResultList == null) {
            throw new GMSException(AttachErrorCode.ATTACH_NOT_FOUND_FILE);
        }

        if(selectResultList.size() == 0) {
            throw new GMSException(AttachErrorCode.ATTACH_NOT_FOUND_FILE);
        }

        result.put(FILE_INFO_PATH_KEY, selectResultList.get(0).getFilePath());
        result.put(FILE_INFO_NAME_KEY, selectResultList.get(0).getAttachContent());
        result.put(FILE_INFO_SIZE_KEY, String.valueOf(selectResultList.get(0).getAttachSize()));

        return result;
    }

    @Override
    public List<AttachStorageDownloadInfoVO> getIsoIntroQualityTypeDownloadInfoList(String isoIntroId) throws Exception {
        AttachVO param = new AttachVO();

        param.setAttachDataType(AttachDataType.ISO_INTRO_QUALITY.getAttachDataTypeCode());
        param.setIsoIntroId(isoIntroId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return attachDAO.selectAttachDownloadInfoList(param);
    }
}
