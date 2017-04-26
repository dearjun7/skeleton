package com.hs.gms.srv.api.system.code;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hs.gms.std.common.controller.GMSCommonController;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.response.CommonResultDataVO;
import com.hs.gms.std.common.response.ResponseVO;

/**
 * CodeController
 * 
 * @author BH Jun
 */
@RestController
public class CodeController extends GMSCommonController {

    @Autowired
    private CodeAccessSource codeAccessSource;
    @Autowired
    private CodeService codeService;

    @RequestMapping(value = "/systems/cache/codes/{pCode}", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public ResponseVO<CodeVO> getTenantCodeListByParentCodeFromCache(@PathVariable String pCode) throws Exception {
        List<CodeVO> result = null;
        CodeResponseWrapperVO responseWrapper = new CodeResponseWrapperVO();

        try {
            result = codeAccessSource.getTenantCodeListFromCache(pCode);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.wrappingResponseDataForXML(super.makeResponseData(HttpStatus.OK, result), responseWrapper);
    }

    @RequestMapping(value = "/systems/cache/syscodes/{pCode}", method = RequestMethod.GET, produces = {"application/xml",
            "application/json"})
    public ResponseVO<CodeVO> getSystemCodeListByParentCodeFromCache(@PathVariable String pCode) throws Exception {
        List<CodeVO> result = null;
        CodeResponseWrapperVO responseWrapper = new CodeResponseWrapperVO();

        try {
            result = codeAccessSource.getSystemCodeListFromCache(pCode);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.wrappingResponseDataForXML(super.makeResponseData(HttpStatus.OK, result), responseWrapper);
    }

    public ResponseVO<CommonResultDataVO> flushAndLoadCodes() {

        try {} catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, super.getProcessSuccessCode());
    }

    @RequestMapping(value = "/systems/codes", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public ResponseVO<CodeVO> getCodeList(HttpServletRequest request,
            @RequestParam(defaultValue = "1", required = false) int currentPageNum,
            @RequestParam(defaultValue = "0", required = false) int pageViewSize) throws Exception {
        List<CodeVO> result = null;
        int totalCount = 0;
        CodeResponseWrapperVO responseWrapper = new CodeResponseWrapperVO();

        try {
            result = codeService.getCodeList(pageViewSize, currentPageNum);
            totalCount = codeService.getCodeTotalCount();
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.wrappingResponseDataForXML(super.makeResponseData(HttpStatus.OK, result), responseWrapper, currentPageNum,
                pageViewSize, totalCount);
    }
}
