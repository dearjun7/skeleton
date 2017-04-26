package com.hs.gms.srv.api.autocomplete;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * AutoCompleteService
 * 
 * @author BH Jun
 */
public interface AutoCompleteService {

    public List<JSONObject> getAutoCompleteResultList(AutoCompleteType autoCompleteType, String tenantId,
            List<AutoCompleteQueryParamVO> queryParamList) throws Exception;

    public List<JSONObject> getAutoCompleteResultList(AutoCompleteType autoCompleteType, List<AutoCompleteQueryParamVO> queryParamList)
            throws Exception;

    public void importCoreDataToAutoCompleteServer(String coreName) throws Exception;
}
