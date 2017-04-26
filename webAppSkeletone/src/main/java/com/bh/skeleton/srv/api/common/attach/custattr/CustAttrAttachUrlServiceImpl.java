package com.hs.gms.srv.api.common.attach.custattr;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hs.gms.srv.api.common.DirtyFlag;
import com.hs.gms.srv.api.common.attach.AttachDAO;
import com.hs.gms.srv.api.common.attach.AttachDownloadService;
import com.hs.gms.srv.api.common.attach.AttachUploadService;
import com.hs.gms.srv.api.common.attach.type.AttachType;
import com.hs.gms.srv.api.common.attach.vo.AttachCustAttrVO;
import com.hs.gms.srv.api.common.attach.vo.AttachStorageDownloadInfoVO;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.srv.api.modeler.proc.ModelerProcCacheService;
import com.hs.gms.srv.api.modeler.vo.ModelerRemoveAttachVO;

/**
 * ProcCustAttrAttachUrl Service Implementation
 * 
 * @author JS Park
 */
@Service
public class CustAttrAttachUrlServiceImpl implements CustAttrAttachUrlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustAttrAttachUrlServiceImpl.class);

    @Autowired
    private AttachDownloadService attachDownloadService;

    @Autowired
    private AttachUploadService attachUploadService;

    @Autowired
    private AttachDAO attachDAO;

    @Autowired
    private ModelerProcCacheService modelerProcCacheService;

    @Override
    public List<AttachCustAttrVO> getProcCustAttrAttachUrls(String procId, String procVersion, String tmpltId, String custAttrId)
            throws Exception {
        List<AttachStorageDownloadInfoVO> downloads = attachDownloadService.getProcTypeDownloadInfoList(procId, procVersion, tmpltId,
                custAttrId);
        List<AttachCustAttrVO> attachUrls = new ArrayList<AttachCustAttrVO>();
        for(AttachStorageDownloadInfoVO download : downloads) {
            AttachCustAttrVO attachUrl = new AttachCustAttrVO();

            attachUrl.setDownloadURL(download.getDownloadURL());
            attachUrl.setAttachId(download.getAttachId());
            attachUrl.setAttachType(download.getAttachType());
            attachUrl.setAttachName(download.getAttachName());
            attachUrl.setAttachContent(download.getAttachContent());
            attachUrl.setAttachSize(download.getAttachSize());

            attachUrl.setProcId(procId);
            attachUrl.setProcVersion(procVersion);
            attachUrl.setTmpltId(tmpltId);
            attachUrl.setCustAttrId(custAttrId);

            attachUrls.add(attachUrl);
        }

        return attachUrls;
    }

    @Override
    public boolean modifyCustAttrAttachUrls(List<AttachCustAttrVO> attachUrls) throws Exception {
        for(AttachCustAttrVO attachUrl : attachUrls) {
            if(DirtyFlag.INSERT.getValue().equals(attachUrl.getDirtyFlag())) {
                createAttachUrl(attachUrl);
            } else if(DirtyFlag.UPDATE.getValue().equals(attachUrl.getDirtyFlag())) {
                modifyAttachUrl(attachUrl);
            } else if(DirtyFlag.DELETE.getValue().equals(attachUrl.getDirtyFlag())) {
                ModelerRemoveAttachVO removeAttachCache = modelerProcCacheService.getRemoveAttachCache();
                if(removeAttachCache != null) {
                    removeAttachCache.getAttachUrls().add(attachUrl);
                    modelerProcCacheService.saveRemoveAttachCache(removeAttachCache);
                } else {
                    removeAttachUrl(attachUrl);
                }

            }
        }

        return true;
    }

    @Override
    public boolean modifyCustAttrAttachUrls(String isoIntroId, List<AttachCustAttrVO> attachUrls) throws Exception {
        for(AttachCustAttrVO attachUrl : attachUrls) {
            if(DirtyFlag.INSERT.getValue().equals(attachUrl.getDirtyFlag())) {
                createAttachUrl(isoIntroId, attachUrl);
            } else if(DirtyFlag.UPDATE.getValue().equals(attachUrl.getDirtyFlag())) {
                modifyAttachUrl(attachUrl);
            } else if(DirtyFlag.DELETE.getValue().equals(attachUrl.getDirtyFlag())) {
                ModelerRemoveAttachVO removeAttachCache = modelerProcCacheService.getRemoveAttachCache();
                if(removeAttachCache != null) {
                    removeAttachCache.getAttachUrls().add(attachUrl);
                    modelerProcCacheService.saveRemoveAttachCache(removeAttachCache);
                } else {
                    removeAttachUrl(attachUrl);
                }

            }
        }

        return true;
    }

    @Override
    public List<AttachCustAttrVO> getObjCustAttrAttachUrls(String objId, String tmpltId, String custAttrId, String procId,
            String procVersion) throws Exception {

        List<AttachStorageDownloadInfoVO> downloads = null;
        if(procId == null) {
            downloads = attachDownloadService.getObjectTypeDownloadInfoList(objId, tmpltId, custAttrId);
        } else {
            downloads = attachDownloadService.getSubObjectTypeDownloadInfoList(procId, procVersion, objId, tmpltId, custAttrId);
        }

        List<AttachCustAttrVO> attachUrls = new ArrayList<AttachCustAttrVO>();
        for(AttachStorageDownloadInfoVO download : downloads) {
            AttachCustAttrVO attachUrl = new AttachCustAttrVO();

            attachUrl.setDownloadURL(download.getDownloadURL());
            attachUrl.setAttachId(download.getAttachId());
            attachUrl.setAttachType(download.getAttachType());
            attachUrl.setAttachName(download.getAttachName());
            attachUrl.setAttachContent(download.getAttachContent());
            attachUrl.setAttachSize(download.getAttachSize());

            attachUrl.setProcId(procId);
            attachUrl.setProcVersion(procVersion);
            attachUrl.setObjId(objId);
            attachUrl.setTmpltId(tmpltId);
            attachUrl.setCustAttrId(custAttrId);

            attachUrls.add(attachUrl);
        }

        return attachUrls;
    }

    private void createAttachUrl(AttachCustAttrVO attachUrl) throws Exception {
        if(AttachType.F.getAttachTypeCode().equals(attachUrl.getAttachType())
                && (attachUrl.getTmpFileKey() == null || "".equals(attachUrl.getTmpFileKey()))) { // 복사

            List<String> targetAttachList = new ArrayList<String>();
            List<String> sourceAttachList = new ArrayList<String>();
            sourceAttachList.add(attachUrl.getAttachId());

            if(attachUrl.getObjId() == null || "".equals(attachUrl.getObjId())) {
                targetAttachList = attachUploadService.copyAttachData(sourceAttachList, attachUrl.getProcId(), attachUrl.getProcVersion());
            } else {
                targetAttachList = attachUploadService.copyAttachData(sourceAttachList, attachUrl.getProcId(), attachUrl.getProcVersion(),
                        attachUrl.getObjId());
            }
            attachUrl.setAttachId(targetAttachList.get(0));

        } else { // 추가
            AttachVO attach = new AttachVO();

            attach.setAttachType(attachUrl.getAttachType());
            attach.setAttachDataType(attachUrl.getAttachDataType());
            attach.setAttachNames(new String[]{attachUrl.getAttachName()});
            attach.setAttachContents(new String[]{attachUrl.getAttachContent()});
            attach.setTmpFileKeys(new String[]{attachUrl.getTmpFileKey()});

            attach.setProcId(attachUrl.getProcId());
            attach.setProcVersion(attachUrl.getProcVersion());
            attach.setObjId(attachUrl.getObjId());
            attach.setTmpltId(attachUrl.getTmpltId());
            attach.setCustAttrId(attachUrl.getCustAttrId());

            if(AttachType.F.getAttachTypeCode().equals(attach.getAttachType())) {
                attachUploadService.createAttachFileFromUploadedTempFile(attach);
            } else {
                attachUploadService.createAttachURL(attach);
            }

            attachUrl.setAttachId(attach.getAttachId());
        }

    }

    private void createAttachUrl(String isoIntroId, AttachCustAttrVO attachUrl) throws Exception {

        if(AttachType.F.getAttachTypeCode().equals(attachUrl.getAttachType())
                && (attachUrl.getTmpFileKey() == null || "".equals(attachUrl.getTmpFileKey()))) { // 복사

            List<String> targetAttachList = new ArrayList<String>();
            List<String> sourceAttachList = new ArrayList<String>();
            sourceAttachList.add(attachUrl.getAttachId());

            if(attachUrl.getObjId() == null || "".equals(attachUrl.getObjId())) {
                targetAttachList = attachUploadService.copyAttachData(sourceAttachList, attachUrl.getProcId(), attachUrl.getProcVersion());
            } else {
                targetAttachList = attachUploadService.copyAttachData(sourceAttachList, attachUrl.getProcId(), attachUrl.getProcVersion(),
                        attachUrl.getObjId());
            }
            attachUrl.setAttachId(targetAttachList.get(0));

        } else { // 추가
            AttachVO attach = new AttachVO();

            attach.setAttachType(attachUrl.getAttachType());
            attach.setAttachDataType(attachUrl.getAttachDataType());
            attach.setAttachNames(new String[]{attachUrl.getAttachName()});
            attach.setAttachContents(new String[]{attachUrl.getAttachContent()});
            attach.setTmpFileKeys(new String[]{attachUrl.getTmpFileKey()});

            attach.setProcId(attachUrl.getProcId());
            attach.setProcVersion(attachUrl.getProcVersion());
            attach.setObjId(attachUrl.getObjId());
            attach.setTmpltId(attachUrl.getTmpltId());
            attach.setCustAttrId(attachUrl.getCustAttrId());

            if(AttachType.F.getAttachTypeCode().equals(attach.getAttachType())) {
                attachUploadService.createAttachFileFromUploadedTempFile(attach);
            } else {
                attachUploadService.createAttachURL(attach);
            }

            attachUrl.setAttachId(attach.getAttachId());
        }

    }

    private void modifyAttachUrl(AttachCustAttrVO attachUrl) throws Exception {
        attachUploadService.modifyAttachData(attachUrl.getAttachId(), attachUrl.getAttachName(), attachUrl.getAttachContent());
    }

    private void removeAttachUrl(AttachCustAttrVO attachUrl) throws Exception {
        attachUploadService.removeAttachData(attachUrl.getAttachId(), attachUrl.getAttachType());
    }

    @Override
    public boolean removeProcCustAttrAttachUrls(String procId, String procVersion) throws Exception {
        ModelerRemoveAttachVO removeAttachCache = modelerProcCacheService.getRemoveAttachCache();
        if(removeAttachCache != null) {
            AttachCustAttrVO attachUrl = new AttachCustAttrVO();
            attachUrl.setProcId(procId);
            attachUrl.setProcVersion(procVersion);
            removeAttachCache.getAttachUrls().add(attachUrl);
            modelerProcCacheService.saveRemoveAttachCache(removeAttachCache);
        } else {
            attachUploadService.removeProcTypeAttachData(procId, procVersion);
        }

        return true;
    }

    @Override
    public boolean removeObjProcDependCustAttrAttachUrls(String procId, String procVersion, String objId) throws Exception {
        ModelerRemoveAttachVO removeAttachCache = modelerProcCacheService.getRemoveAttachCache();
        if(removeAttachCache != null) {
            AttachCustAttrVO attachUrl = new AttachCustAttrVO();
            attachUrl.setProcId(procId);
            attachUrl.setProcVersion(procVersion);
            attachUrl.setObjId(objId);
            removeAttachCache.getAttachUrls().add(attachUrl);
            modelerProcCacheService.saveRemoveAttachCache(removeAttachCache);
        } else {
            attachUploadService.removeSubObjTypeAttachData(procId, procVersion, objId);
        }

        return true;
    }

    @Override
    public boolean removeObjCustAttrAttachUrls(String objId) throws Exception {
        return attachUploadService.removeObjTypeAttachData(objId);
    }

    @Override
    public boolean createCopyProcCustAttrAttachUrls(String srcProcId, String srcProcVersion, String tarProcId, String tarProcVersion)
            throws Exception {
        if(tarProcId != null) {
            attachUploadService.copyProcTypeAttachData(srcProcId, srcProcVersion, tarProcId, tarProcVersion);
        } else {
            attachUploadService.copyProcTypeAttachData(srcProcId, srcProcVersion, tarProcVersion);
        }

        return true;
    }

    @Override
    public boolean createCopyObjCustAttrAttachUrls(String srcObjId, String srcCustAttrId, String srcProcId, String srcProcVersion,
            String tarObjId, String tarProcId, String tarProcVersion) throws Exception {
        if(tarProcId != null) {
            attachUploadService.copySubObjTypeAttachData(srcObjId, srcCustAttrId, srcProcId, srcProcVersion, tarObjId, tarProcId,
                    tarProcVersion);
        } else {
            attachUploadService.copySubObjTypeAttachData(srcObjId, srcCustAttrId, srcProcId, srcProcVersion, tarProcVersion);
        }

        return true;
    }

    @Override
    public List<AttachCustAttrVO> getProcCustAttrAttachUrls(String procId, String procVersion) throws Exception {
        return attachDAO.selectProcCustAttrAttachUrls(procId, procVersion);
    }

}
