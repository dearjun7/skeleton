package com.hs.gms.srv.api.common.attach.custattr;

import java.util.List;

import com.hs.gms.srv.api.common.attach.vo.AttachCustAttrVO;

public interface CustAttrAttachUrlService {

    public List<AttachCustAttrVO> getProcCustAttrAttachUrls(String procId, String procVersion, String tmpltId, String custAttrId)
            throws Exception;

    public List<AttachCustAttrVO> getObjCustAttrAttachUrls(String objId, String tmpltId, String custAttrId, String procId,
            String procVersion) throws Exception;

    public List<AttachCustAttrVO> getProcCustAttrAttachUrls(String procId, String procVersion) throws Exception;

    public boolean modifyCustAttrAttachUrls(List<AttachCustAttrVO> attachUrls) throws Exception;

    public boolean modifyCustAttrAttachUrls(String isoIntroId, List<AttachCustAttrVO> attachUrls) throws Exception;

    public boolean removeProcCustAttrAttachUrls(String procId, String procVersion) throws Exception;

    public boolean removeObjProcDependCustAttrAttachUrls(String procId, String procVersion, String objId) throws Exception;

    public boolean removeObjCustAttrAttachUrls(String objId) throws Exception;

    public boolean createCopyProcCustAttrAttachUrls(String srcProcId, String srcProcVersion, String tarProcId, String tarProcVersion)
            throws Exception;

    public boolean createCopyObjCustAttrAttachUrls(String srcObjId, String srcCustAttrId, String srcProcId, String srcProcVersion,
            String tarObjId, String tarProcId, String tarProcVersion) throws Exception;

}
