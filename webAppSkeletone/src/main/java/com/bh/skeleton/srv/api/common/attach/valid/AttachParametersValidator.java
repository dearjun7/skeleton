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
 * BH Jun 2016. 10. 31. First Draft
 */
package com.hs.gms.srv.api.common.attach.valid;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.std.common.error.BindResultExceptionRaiser;
import com.hs.gms.std.common.error.CommonErrorCode;
import com.hs.gms.std.common.error.GMSException;

/**
 * AttachParametersValidator.java
 * 
 * @author BH Jun
 */
@Component
public class AttachParametersValidator {

    private final String attachDataTypeFieldName = "attachDataType";
    private final String attachTypeFieldName = "attachType";
    private final List<String> procAttachTypeFieldName = Arrays.asList("procId", "procVersion", "tmpltId", "custAttrId");
    private final List<String> objectAttachTypeFieldName = Arrays.asList("objId", "tmpltId", "custAttrId");
    private final List<String> subObjectAttachTypeFieldName = Arrays.asList("procId", "procVersion", "objId", "tmpltId", "custAttrId");
    private final List<String> changeReqAttachTypeFieldName = Arrays.asList("workRequestId");
    private final List<String> bultAttachTypeFieldName = Arrays.asList("bultId");
    private final List<String> isoAttachTypeFieldName = Arrays.asList("procId", "procVersion", "isoLang");
    private final List<String> isoIntroDocAttachTypeFieldName = Arrays.asList("procId", "isoIntroId");
    private final List<String> isoIntroAttachTypeFieldName = Arrays.asList("workRequestId", "workSeq");
    private final List<String> isoIntroQualityAttachTypeFieldName = Arrays.asList("isoIntroId");

    /**
     * 첨부 타입 속성에 따른 Request Parameter Validation Check 로직
     * <br>
     * - String attachDataType 코드 설명 -<br>
     * '1' : 프로세스 사용자 정의 속성 템플릿
     * <br>
     * '2' : 오브젝트 사용자 정의 속성 템플릿
     * <br>
     * '3' : 종속 오브젝트 사용자 정의 속성 템플릿
     * <br>
     * '4' : 변경요청/이슈 요청
     * <br>
     * '5' : 게시판
     * <br>
     * '6' : ISO
     * <br>
     * '7' : ISO 인증 도입 문서 체계표
     * <br>
     * '8' : ISO 인증 도입
     * <br>
     * '9' : ISO 인증 품질 방침
     * 
     * @param attachDataType
     *            String
     * @param bindingResult
     *            BindingResult
     */
    public void checkAttachRequestValidation(AttachVO attachVO, BindingResult bindingResult) {
        this.checkAttachRequestValidation(attachVO, bindingResult, true);
    }

    public void checkAttachRequestValidation(AttachVO attachVO, BindingResult bindingResult, boolean checkAttachType) {
        if(bindingResult.hasErrors()) {
            BindResultExceptionRaiser.raiseBindingResultException(attachDataTypeFieldName, bindingResult);

            if(checkAttachType) {
                BindResultExceptionRaiser.raiseBindingResultException(attachTypeFieldName, bindingResult);
            }
        }

        switch(attachVO.getAttachDataType()) {
            case "1":
                BindResultExceptionRaiser.raiseBindingResultException(procAttachTypeFieldName, bindingResult);
                break;
            case "2":
                BindResultExceptionRaiser.raiseBindingResultException(objectAttachTypeFieldName, bindingResult);
                break;
            case "3":
                BindResultExceptionRaiser.raiseBindingResultException(subObjectAttachTypeFieldName, bindingResult);
                break;
            case "4":
                BindResultExceptionRaiser.raiseBindingResultException(changeReqAttachTypeFieldName, bindingResult);
                break;
            case "5":
                BindResultExceptionRaiser.raiseBindingResultException(bultAttachTypeFieldName, bindingResult);
                break;
            case "6":
                BindResultExceptionRaiser.raiseBindingResultException(isoAttachTypeFieldName, bindingResult);
                break;
            case "7":
                BindResultExceptionRaiser.raiseBindingResultException(isoIntroDocAttachTypeFieldName, bindingResult);
                break;
            case "8":
                BindResultExceptionRaiser.raiseBindingResultException(isoIntroAttachTypeFieldName, bindingResult);
                break;
            case "9":
                BindResultExceptionRaiser.raiseBindingResultException(isoIntroQualityAttachTypeFieldName, bindingResult);
                break;
            default:
                throw new GMSException(CommonErrorCode.BAD_REQUEST);
        }
    }
}
