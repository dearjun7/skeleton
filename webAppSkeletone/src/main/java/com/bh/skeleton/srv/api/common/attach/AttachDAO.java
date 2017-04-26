package com.hs.gms.srv.api.common.attach;

import java.util.List;

import com.hs.gms.srv.api.common.attach.vo.AttachCustAttrVO;
import com.hs.gms.srv.api.common.attach.vo.AttachStorageDownloadInfoVO;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;

/**
 * AttachDAO
 * 
 * @author BH Jun
 */
public interface AttachDAO {

    public void setApiDomain(String apiDomain);

    public String insertAttachData(AttachVO attachVO);

    public int insertCopyProcAttach(String procId, String procVersion, String newProcVersion);

    public boolean updateAttachData(String attachId, String attachName, String attachContent, String tenantId);

    public boolean updateAttachDataForIso(String procId, String srcProcVersion, String tarProcVersion, String lang, String tenantId);

    public boolean updateAttachDataForIsoIntro(AttachVO attachVO);

    public boolean deleteAttachData(String attachId) throws Exception;

    public boolean deleteAttachData(String attachId, boolean isTargetTenant) throws Exception;

    public boolean deleteAttachData(AttachVO attachVO) throws Exception;

    public boolean deleteAttachData(AttachVO attachVO, boolean isTargetTenant) throws Exception;

    public int deleteAttachFromCopiedProc(String procId, String procVersion);

    public List<AttachVO> selectAttachData(AttachVO attachVO);

    public List<AttachVO> selectAttachData(AttachVO attachVO, boolean isIncludeWebEditor);

    public List<AttachVO> selectAttachData(AttachVO attachVO, boolean isIncludeWebEditor, boolean isTargetTenant);

    public List<AttachVO> selectAttachData(String tenantId);

    public AttachVO selectAttachWebEditorHTML(AttachVO attachVO);

    public List<AttachStorageDownloadInfoVO> selectAttachDownloadInfoList(AttachVO attachVO);

    public List<AttachCustAttrVO> selectProcCustAttrAttachUrls(String procId, String procVersion);

}
