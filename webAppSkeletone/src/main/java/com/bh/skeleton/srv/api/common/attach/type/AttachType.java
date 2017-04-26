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
 * BH Jun 2016. 5. 12. First Draft
 */
package com.hs.gms.srv.api.common.attach.type;

import javax.servlet.http.HttpServletRequest;

import com.hs.gms.srv.api.common.attach.AttachUploadService;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;

/**
 * GMS_ATTACH DB Table의 attachType 컬럼에서 사용할 AttachType 상수 Enum
 * F - File Type
 * U - URL Type
 * 
 * @author BH Jun
 */
public enum AttachType {
    /**
     * File 타입
     */
    F {

        @Override
        public void createAttachData(AttachUploadService attachService, HttpServletRequest request, AttachVO attachVO) throws Exception {
            attachService.createAttachFile(request, attachVO);
        }
    },
    /**
     * URL 타입
     */
    U {

        @Override
        public void createAttachData(AttachUploadService attachService, HttpServletRequest request, AttachVO attachVO) throws Exception {
            attachService.createAttachURL(attachVO);
        }
    },
    /**
     * WebEditor 타입
     */
    W {

        @Override
        public void createAttachData(AttachUploadService attachService, HttpServletRequest request, AttachVO attachVO) throws Exception {
            throw new UnsupportedOperationException();
        }

    };

    public abstract void createAttachData(AttachUploadService attachService, HttpServletRequest request, AttachVO attachVO)
            throws Exception;

    public String getAttachTypeCode() {
        return this.toString();
    }
}
