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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hs.gms.srv.api.common.attach.vo.AttachVO;

/**
 * WebEditorService.java
 * 
 * @author BH Jun
 */
public interface WebEditorService {

    public static final String WEB_EDITOR_STORE_ROOT_DIR = "/home/handy/files";

    /**
     * <b>웹 에디터 편집 화면 출력</b><br>
     * html로 구성된 웹에디터 편집 툴을 요청자에게 응답보낸다.
     * 
     * @param request
     * @param attachVO
     * @throws Exception
     */
    public void getWebEditor(HttpServletRequest request) throws Exception;

    /**
     * <b>웹 에디터 파일 정보 Attach 등록</b><br>
     * 웹 에디터 파일의 등록 정보를 attachVO 객체의 String[] webEditorTmpFileKeys 필드를 통하여 cache
     * 서버(Redis)로부터 읽어와 gms_attach 테이블에 등록한다.
     * 
     * @param attachVO
     *            AttachVO
     * @return webEditorAttachId String
     * @throws Exception
     */
    public String createAttachDataForWebEditor(AttachVO attachVO) throws Exception;

    /**
     * <b>저장된 웹 에디터 html response</b><br>
     * attach 테이블에 저장된 웹에디터파일을 읽어와 html로 응답보낸다.
     * 
     * @param attachVO
     * @throws Exception
     */
    public void getWebEditorRegisteredView(AttachVO attachVO) throws Exception;

    /**
     * <b>웹 에디터 저장 정보 temp cache 저장 </b><br>
     * 웹에디터의 저장 정보를 cache 서버(redis)에 저장하고 webTempFileKey 리스트를 반환한다.
     * 
     * @param request
     * @return webTempFileKeyList List&lt;String&gt;
     * @throws Exception
     */
    public List<String> createTempWebEditorFiles(HttpServletRequest request) throws Exception;

    public void convertHtmlAttachedImageDownloadPath(String htmlPath, List<Map<String, String>> convertLinkInfoList) throws Exception;
}
