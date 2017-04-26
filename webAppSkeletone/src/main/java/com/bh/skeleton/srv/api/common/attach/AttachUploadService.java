package com.hs.gms.srv.api.common.attach;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;

/**
 * AttachUploadService
 * 
 * @author BH Jun
 */
public interface AttachUploadService {

    /**
     * 첨부 파일 temp 저장
     * 
     * @param request
     *            HttpServletRequest
     * @return List - 업로드된 temp 파일 결과
     * @throws Exception
     */
    public List<FileStorageUploadResultVO> createAttachTempFile(HttpServletRequest request) throws Exception;

    /**
     * 첨부 파일 저장
     * 
     * @param request
     *            HttpServletRequest
     * @param attachVO
     *            AttachVO
     * @return boolean
     * @throws Exception
     */
    public boolean createAttachFile(HttpServletRequest request, AttachVO attachVO) throws Exception;

    /**
     * Temp 경로에 저장된 첨부 파일 저장
     * 
     * @param attachVO
     *            AttachVO
     * @return boolean
     * @throws Exception
     */
    public boolean createAttachFileFromUploadedTempFile(AttachVO attachVO) throws Exception;

    /**
     * URL 첨부 저장
     * 
     * @param attachVO
     *            AttachVO
     * @return boolean
     * @throws Exception
     */
    public boolean createAttachURL(AttachVO attachVO) throws Exception;

    /**
     * 첨부 복사
     * 
     * @param sourceAttachIdList
     *            List<String> - 복사할 대상 attachId List
     * @param destProcId
     *            String - 복사 시 생성되는 procId
     * @param destProcVersion
     *            String - 복사 시 생성되는 procVersion
     * @return List<String> - 복사된 attachId List
     * @throws Exception
     */
    public List<String> copyAttachData(List<String> sourceAttachIdList, String destProcId, String destProcVersion) throws Exception;

    /**
     * 첨부 복사
     * 
     * @param sourceAttachIdList
     *            List<String> - 복사할 대상 attachId List
     * @param destProcId
     *            String - 복사 시 생성되는 procId
     * @param destProcVersion
     *            String - 복사 시 생성되는 procVersion
     * @param destObjId
     *            String - 복사시 생성되는 objId
     * @return List<String> - 복사된 attachId List
     * @throws Exception
     */
    public List<String> copyAttachData(List<String> sourceAttachIdList, String destProcId, String destProcVersion, String destObjId)
            throws Exception;

    /**
     * Process 타입 버전 변경에 따른 파일 첨부 복사
     * 
     * @param procId
     *            String - 복사할 대상의 proId
     * @param sourceProcVersion
     *            String - 복사할 대상의 procVersion
     * @param destProcVersion
     *            String - 새로 변경할 procVersion
     * @throws Exception
     */
    public void copyProcTypeAttachData(String procId, String sourceProcVersion, String destProcVersion) throws Exception;

    /**
     * Process 복사에 따른 파일 첨부 복사
     * 
     * @param sourceProcId
     *            String - 복사할 대상의 procId
     * @param sourceProcVersion
     *            String - 복사할 대상의 procVersion
     * @param destProcId
     *            String - 새로 복사되어 생성될 procId
     * @param destProcVersion
     *            String - 새로 복사되어 생성될 procVersion
     * @throws Exception
     */
    public void copyProcTypeAttachData(String sourceProcId, String sourceProcVersion, String destProcId, String destProcVersion)
            throws Exception;

    /**
     * 종속 Object 타입 - 버전 변경에 따른 파일 첨부 복사
     * 
     * @param objId
     *            String - 복사할 대상의 objId
     * @param custAttrId
     *            String - 복사할 대상의 custAttrId
     * @param procId
     *            String - 복사할 대상의 proId
     * @param sourceProcVersion
     *            String - 복사할 대상의 procVersion
     * @param destProcVersion
     *            String - 새로 변경할 procVersion
     * @throws Exception
     */
    public void copySubObjTypeAttachData(String objId, String custAttrId, String procId, String sourceProcVersion, String destProcVersion)
            throws Exception;

    /**
     * 종속 Object 타입 - 복사에 따른 파일 첨부 복사
     * 
     * @param sourceObjId
     *            String - 복사할 대상의 objId
     * @param sourceCustAttrId
     *            String - 복사할 대상의 custAttrId
     * @param sourceProcId
     *            String - 복사할 대상의 proId
     * @param sourceProcVersion
     *            String - 복사할 대상의 procVersion
     * @param destObjId
     *            String - 새로 복사되어 생성될 objId
     * @param destProcId
     *            String - 새로 복사되어 생성될 procId
     * @param destProcVersion
     *            String - 새로 복사되어 생성될 procVersion
     * @throws Exception
     */
    public void copySubObjTypeAttachData(String sourceObjId, String sourceCustAttrId, String sourceProcId, String sourceProcVersion,
            String destObjId, String destProcId, String destProcVersion) throws Exception;

    /**
     * ISO 타입 - 복사에 따른 파일 첨부 복사
     * 
     * @param sourceProcId
     *            String - 복사할 대상의 proId
     * @param sourceProcVersion
     *            String - 복사할 대상의 procVersion
     * @param sourceLang
     *            String - 복사할 대상의 isoLang
     * @param destProcId
     *            String - 새로 복사되어 생성될 procId
     * @param destProcVersion
     *            String - 새로 복사되어 생성될 procVersion
     * @throws Exception
     */
    public void copyISOAttachData(String sourceProcId, String sourceProcVersion, String isoLang, String destProcId, String destProcVersion)
            throws Exception;

    /**
     * 첨부 삭제
     * 
     * @param attachId
     *            String
     * @param attachType
     *            String
     * @return boolean
     * @throws Exception
     */
    public boolean removeAttachData(String attachId, String attachType) throws Exception;

    /**
     * 테넌트 첨부 삭제
     * 
     * @param tenantId
     * @param attachId
     * @param attachType
     * @return
     * @throws Exception
     */
    public boolean removeAttachData(String tenantId, String attachId, String attachType) throws Exception;

    /**
     * 프로세스 타입 첨부 삭제
     * 
     * @param procId
     * @param procVersion
     * @return
     * @throws Exception
     */
    public boolean removeProcTypeAttachData(String procId, String procVersion) throws Exception;

    /**
     * ISO 타입 첨부 삭제
     * 
     * @param procId
     * @param procVersion
     * @param lang
     * @return
     * @throws Exception
     */
    public boolean removeProcTypeAttachData(String procId, String procVersion, String lang) throws Exception;

    /**
     * 오브젝트 타입 첨부 삭제
     * 
     * @param objId
     * @return
     * @throws Exception
     */
    public boolean removeObjTypeAttachData(String objId) throws Exception;

    /**
     * 종속 오브젝트 타입 첨부 삭제
     * 
     * @param procId
     * @param procVersion
     * @param objId
     * @return
     * @throws Exception
     */
    public boolean removeSubObjTypeAttachData(String procId, String procVersion, String objId) throws Exception;

    /**
     * 첨부 정보 수정
     * 
     * @param attachId
     * @param attachName
     * @param attachContent
     * @return
     * @throws Exception
     */
    public boolean modifyAttachData(String attachId, String attachName, String attachContent) throws Exception;

    /**
     * temp 파일 업로드 후에 return된 fileStorageUploadResultVO 정보로 생성된 tempFileKey 가져오기
     * 
     * @param fileStorageUploadResultVO
     *            FileStorageUploadResultVO - tempFile 정보
     * @return tempFileKey String
     * @throws Exception
     */
    public String getAttachTempFileKey(FileStorageUploadResultVO fileStorageUploadResultVO) throws Exception;

    public List<String> setFileStorageUploadResultToTempCache(List<FileStorageUploadResultVO> tempFileList) throws Exception;

    public List<FileStorageUploadResultVO> getTempFileDataListToCache(String[] tempFileKeyArr) throws Exception;
}
