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

/**
 * GMS_ATTACH DB Table의 attachDataType 컬럼에서 사용할 AttachDataType 상수 Enum.
 * 
 * @author BH Jun
 */
public enum AttachDataType {
    /**
     * 프로세스 사용자 정의 속성
     */
    PROCESS("1"),
    /**
     * 오브젝트 사용자 정의 속성
     */
    OBJECT("2"),
    /**
     * 종속 오브젝트 사용자 정의 속성
     */
    SUB_OBJECT("3"),
    /**
     * 변경 요청/이슈요청
     */
    CHANGE_REQUEST("4"),
    /**
     * 게시판
     */
    BORAD("5"),
    /**
     * ISO
     */
    ISO("6"),
    /**
     * ISO 인증 도입 문서 체계표
     */
    ISO_INTRO_DOC("7"),
    /**
     * ISO 인증 도입
     */
    ISO_INTRO("8"),
    /**
     * ISO 인증 품질 방침
     */
    ISO_INTRO_QUALITY("9");

    private String attachDataTypeCode;

    AttachDataType(String attachDataTypeCode) {
        this.setAttachDataTypeCode(attachDataTypeCode);
    }

    /**
     * Attach Data Type 속성 코드 가져오기
     * 
     * @return String attachDataTypeCode
     */
    public String getAttachDataTypeCode() {
        return attachDataTypeCode;
    }

    public void setAttachDataTypeCode(String attachDataTypeCode) {
        this.attachDataTypeCode = attachDataTypeCode;
    }
}
