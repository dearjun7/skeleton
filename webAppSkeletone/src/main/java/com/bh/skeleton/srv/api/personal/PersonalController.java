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
 * BH Jun 2017. 1. 13. First Draft
 */
package com.hs.gms.srv.api.personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hs.gms.srv.api.personal.language.PersonalLanguageService;
import com.hs.gms.std.common.controller.GMSCommonController;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.response.CommonResultDataVO;
import com.hs.gms.std.common.response.ResponseVO;

/**
 * PersonalController.java
 * 
 * @author BH Jun
 */
@RestController
public class PersonalController extends GMSCommonController {

    @Autowired
    private PersonalLanguageService personalLanguageService;

    @RequestMapping(value = "/persons/language", method = RequestMethod.GET, produces = "application/json")
    public ResponseVO<CommonResultDataVO> getUserLanguage() throws Exception {
        CommonResultDataVO result = new CommonResultDataVO();

        try {
            result.setResult(personalLanguageService.getUserLanguage());
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result);
    }

    @RequestMapping(value = "/persons/language", method = RequestMethod.PUT, produces = "application/json")
    public ResponseVO<CommonResultDataVO> modifyUserLanguage(@RequestParam(required = true) String language) throws Exception {

        try {
            personalLanguageService.modifyUserLanguage(language);
        } catch(Exception e) {
            throw new GMSException(e);
        }
        return super.makeResponseData(HttpStatus.OK, super.getProcessSuccessCode());
    }
}
