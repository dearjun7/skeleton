package com.hs.gms.srv.api.common.attach;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.common.attach.vo.AttachCustAttrVO;
import com.hs.gms.srv.api.common.attach.vo.AttachStorageDownloadInfoVO;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.std.common.dao.GMSCommonDAO;
import com.hs.gms.std.common.dao.SequenceTable;
import com.hs.gms.std.common.error.CommonErrorCode;
import com.hs.gms.std.common.error.GMSException;

/**
 * AttachDAOImpl
 * 
 * @author BH Jun
 */
@Repository
public class AttachDAOImpl extends GMSCommonDAO implements AttachDAO {

    @Value("#{config['gms.filestorage.download.context']}")
    private String downloadURIContext;
    @Value("#{config['gms.common.api.domain']}")
    private String defaultApiDomain;
    private String apiDomain;

    @PostConstruct
    public void init() {
        this.apiDomain = this.defaultApiDomain;
    }

    @Override
    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    @Override
    public String insertAttachData(AttachVO param) {
        String attachId = super.selectSequence(SequenceTable.ATTACH);
        boolean result = false;

        param.setAttachId(attachId);
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        if(param.getOrgAttachId() == null || "".equals(param.getOrgAttachId())) {
            param.setOrgAttachId(attachId);
        }

        result = super.getSqlSession().insert("common.attach.attach.insertAttach", param) > 0 ? true : false;

        return result ? attachId : null;
    }

    @Override
    public int insertCopyProcAttach(String procId, String procVersion, String newProcVersion) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("procId", procId);
        params.put("procVersion", procVersion);
        params.put("newProcVersion", newProcVersion);
        params.put("tenantId", UserDataContextHolder.getUserData().getTenantId());

        return super.getSqlSession().insert("common.attach.attach.insertCopyProcAttach", params);
    }

    @Override
    public boolean updateAttachData(String attachId, String attachName, String attachContent, String tenantId) {
        AttachVO param = new AttachVO();

        param.setAttachId(attachId);
        param.setTenantId(tenantId);
        param.setAttachName(attachName);
        param.setAttachContent(attachContent);

        return super.getSqlSession().update("common.attach.attach.updateAttach", param) > 0 ? true : false;
    }

    @Override
    public boolean updateAttachDataForIso(String procId, String srcProcVersion, String tarProcVersion, String lang, String tenantId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("procId", procId);
        params.put("srcProcVersion", srcProcVersion);
        params.put("tarProcVersion", tarProcVersion);
        params.put("isoLang", lang);
        params.put("tenantId", UserDataContextHolder.getUserData().getTenantId());

        return super.getSqlSession().update("common.attach.attach.updateAttachForIso", params) > 0 ? true : false;
    }

    @Override
    public boolean updateAttachDataForIsoIntro(AttachVO attachVO) {

        attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return super.getSqlSession().update("common.attach.attach.updateAttachForIsoIntro", attachVO) > 0 ? true : false;
    }

    @Override
    public boolean deleteAttachData(String attachId) throws Exception {
        return this.deleteAttachData(attachId, false);
    }

    @Override
    public boolean deleteAttachData(String attachId, boolean isTargetTenant) throws Exception {
        AttachVO param = new AttachVO();

        param.setAttachId(attachId);

        return this.deleteAttachData(param, isTargetTenant);
    }

    @Override
    public boolean deleteAttachData(AttachVO attachVO) throws Exception {
        return this.deleteAttachData(attachVO, false);
    }

    @Override
    public boolean deleteAttachData(AttachVO attachVO, boolean isTargetTenant) throws Exception {
        if(!isTargetTenant) {
            attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        }

        if(attachVO.getAttachId() == null) {
            if(attachVO.getAttachDataType() == null) {
                throw new GMSException(CommonErrorCode.BAD_REQUEST);
            }
        }

        return super.getSqlSession().delete("common.attach.attach.deleteAttach", attachVO) > 0 ? true : false;
    }

    @Override
    public int deleteAttachFromCopiedProc(String procId, String procVersion) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("procId", procId);
        params.put("procVersion", procVersion);
        params.put("tenantId", UserDataContextHolder.getUserData().getTenantId());

        return super.getSqlSession().delete("common.attach.attach.deleteAttachFromCopiedProc", params);
    }

    @Override
    public List<AttachVO> selectAttachData(AttachVO param) {
        return this.selectAttachData(param, false);
    }

    @Override
    public List<AttachVO> selectAttachData(AttachVO attachVO, boolean isIncludeWebEditor) {
        return selectAttachData(attachVO, isIncludeWebEditor, false);
    }

    @Override
    public List<AttachVO> selectAttachData(AttachVO attachVO, boolean isIncludeWebEditor, boolean isTargetTenant) {
        Map<String, Object> params = new HashMap<String, Object>();

        if(!isTargetTenant) {
            attachVO.setTenantId(UserDataContextHolder.getUserData().getTenantId());
        }

        params.put("attach", attachVO);
        params.put("isIncludeWebEditor", isIncludeWebEditor);

        return super.getSqlSession().selectList("common.attach.attach.selectAttach", params);
    }

    @Override
    public List<AttachVO> selectAttachData(String tenantId) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("tenantId", tenantId);

        return super.getSqlSession().selectList("common.attach.attach.selectAttachByTenantId", params);
    }

    @Override
    public AttachVO selectAttachWebEditorHTML(AttachVO param) {
        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        return super.getSqlSession().selectOne("common.attach.attach.selectAttachWebEditorHTML", param);
    }

    @Override
    public List<AttachStorageDownloadInfoVO> selectAttachDownloadInfoList(AttachVO param) {
        Map<String, Object> queryParam = new HashMap<String, Object>();

        param.setTenantId(UserDataContextHolder.getUserData().getTenantId());

        queryParam.put("downloadURIContext", downloadURIContext);
        queryParam.put("apiDomain", apiDomain);
        queryParam.put("param", param);

        return super.getSqlSession().selectList("common.attach.attach.selectAttachIdList", queryParam);
    }

    @Override
    public List<AttachCustAttrVO> selectProcCustAttrAttachUrls(String procId, String procVersion) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("procId", procId);
        params.put("procVersion", procVersion);
        params.put("lang", UserDataContextHolder.getUserData().getLanguage());
        params.put("tenantId", UserDataContextHolder.getUserData().getTenantId());
        params.put("userId", UserDataContextHolder.getUserData().getUserId());
        params.put("downloadURIContext", downloadURIContext);
        params.put("apiDomain", apiDomain);

        return super.getSqlSession().selectList("common.attach.attach.selectProcCustAttrAttachUrl", params);
    }
}
