package com.hs.gms.srv.api.autocomplete;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.controller.GMSCommonController;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.response.ResponseVO;

import net.sf.json.JSONObject;

/**
 * AutoCompleteController
 * 
 * @author BH Jun
 */
@RestController
public class AutoCompleteController extends GMSCommonController {

    @Autowired
    private AutoCompleteService autoCompleteService;

    @RequestMapping(value = "/autocomplete/codename", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseVO<JSONObject> getAutoCompleteCodeName(@RequestParam(required = true) String pCode,
            @RequestParam(required = true) String codeName) {
        List<JSONObject> queryResult = null;
        List<AutoCompleteQueryParamVO> queryParamList = new ArrayList<AutoCompleteQueryParamVO>();
        UserDataVO userData = UserDataContextHolder.getUserData();
        AutoCompleteQueryParamVO codeQueryParam = new AutoCompleteQueryParamVO();
        AutoCompleteQueryParamVO pCodeParam = new AutoCompleteQueryParamVO();

        codeQueryParam.setParamName("codeName" + "_" + userData.getLanguage().split("_")[0]);
        codeQueryParam.setParamValue(codeName);
        pCodeParam.setParamName("pCode");
        pCodeParam.setParamValue(pCode);

        queryParamList.add(codeQueryParam);
        queryParamList.add(pCodeParam);

        try {
            queryResult = autoCompleteService.getAutoCompleteResultList(AutoCompleteType.CODE, userData.getTenantId(), queryParamList);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, queryResult);
    }

    @RequestMapping(value = "/autocomplete/username", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseVO<JSONObject> getAutoCompleteUserName(@RequestParam(required = true) String name,
            @RequestParam(defaultValue = "false", required = false) boolean isNotIncludeDept) {
        List<JSONObject> queryResult = null;
        List<AutoCompleteQueryParamVO> queryParamList = new ArrayList<AutoCompleteQueryParamVO>();
        UserDataVO userData = UserDataContextHolder.getUserData();
        AutoCompleteQueryParamVO userQueryParam = new AutoCompleteQueryParamVO();

        userQueryParam.setParamName("name" + "_" + userData.getLanguage().split("_")[0]);
        userQueryParam.setParamValue(name);

        queryParamList.add(userQueryParam);

        if(isNotIncludeDept) {
            AutoCompleteQueryParamVO userDataTypeQueryParam = new AutoCompleteQueryParamVO();

            userDataTypeQueryParam.setParamName("dataType");
            userDataTypeQueryParam.setParamValue("U");

            queryParamList.add(userDataTypeQueryParam);
        }

        try {
            queryResult = autoCompleteService.getAutoCompleteResultList(AutoCompleteType.USER, userData.getTenantId(), queryParamList);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, queryResult);
    }

}
