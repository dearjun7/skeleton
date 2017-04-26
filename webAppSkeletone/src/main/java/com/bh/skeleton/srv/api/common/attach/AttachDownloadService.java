package com.hs.gms.srv.api.common.attach;

import java.util.List;

import com.hs.gms.srv.api.common.attach.vo.AttachStorageDownloadInfoVO;

/**
 * AttachDownloadService
 * 
 * @author BH Jun
 */
public interface AttachDownloadService {

    public void setApiDomain(String apiDomain);

    public void getAttachFileDownload(String attachId) throws Exception;

    public void getCompressedAttachFileDownload(List<String> attachIdList) throws Exception;

    public void getAttachTempFileDownload(String tmpFileKey) throws Exception;

    public List<AttachStorageDownloadInfoVO> getProcTypeDownloadInfoList(String procId, String procVersion, String tmpltId,
            String custAttrId) throws Exception;

    public List<AttachStorageDownloadInfoVO> getObjectTypeDownloadInfoList(String objId, String tmpltId, String custAttrId)
            throws Exception;

    public List<AttachStorageDownloadInfoVO> getSubObjectTypeDownloadInfoList(String procId, String procVersion, String objId,
            String tmpltId, String custAttrId) throws Exception;

    public List<AttachStorageDownloadInfoVO> getChangeRequestTypeDownloadInfoList(String workRequestId) throws Exception;

    public List<AttachStorageDownloadInfoVO> getBultTypeDownloadInfoList(String bultId) throws Exception;

    //    public List<AttachStorageDownloadInfoVO> getBoardTypeDownloadInfoList(String attachId) throws Exception;
    //
    //    public List<AttachStorageDownloadInfoVO> getISOCheckListTypeDownloadInfoList(String attachId) throws Exception;

    public List<AttachStorageDownloadInfoVO> getIsoIntroQualityTypeDownloadInfoList(String isoIntroId) throws Exception;
}
