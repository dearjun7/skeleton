package com.hs.gms.srv.api.autocomplete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hs.gms.std.common.service.http.HttpConnector;
import com.hs.gms.std.common.util.JSONConverter;

import net.sf.json.JSONObject;

/**
 * AutoCompleteServiceImpl
 * 
 * @author BH Jun
 */
@Service("AutoCompleteService")
public class AutoCompleteServiceImpl implements AutoCompleteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoCompleteService.class);

    @Value("#{config['gms.common.charset']}")
    private String charset;
    @Value("#{config['gms.solr.connection.host']}")
    private String autoCompleteUrl;

    @Autowired
    private HttpConnector httpConnect;

    @Override
    public List<JSONObject> getAutoCompleteResultList(AutoCompleteType autoCompleteType, List<AutoCompleteQueryParamVO> queryParamList)
            throws Exception {
        return getAutoCompleteResultList(autoCompleteType, null, queryParamList);
    }

    @Override
    public List<JSONObject> getAutoCompleteResultList(AutoCompleteType autoCompleteType, String tenantId,
            List<AutoCompleteQueryParamVO> queryParamList) throws Exception {
        JSONObject response = JSONObject.fromObject("{docs:[]}");

        if(queryParamList != null) {
            StringBuilder queryWordsBuilder = new StringBuilder();
            int idx = 0;

            for(AutoCompleteQueryParamVO tmpQueryParam : queryParamList) {
                String tmpQueryValue = StringUtils.stripStart(tmpQueryParam.getParamValue(), null);

                if(idx != 0) {
                    queryWordsBuilder.append(" AND ");
                }

                if(!"".equals(tmpQueryValue.trim())) {
                    queryWordsBuilder.append(this.makeQueryWords(tmpQueryParam.getParamName(), tmpQueryValue.split(" ")));
                    idx++;
                }
            }

            LOGGER.debug("getAutoCompletequeryWords : " + queryWordsBuilder.toString());

            JSONObject callResult = this.getAutoCompleteResultList(autoCompleteType, queryWordsBuilder.toString(), tenantId, queryParamList,
                    true);
            response = callResult.getJSONObject("response").getString("docs").equals("") ? response : callResult.getJSONObject("response");

            LOGGER.debug("getAutoCompleteResultList : " + response);
        }

        return JSONConverter.jsonObjectToList(response, "docs", JSONObject.class);
    }

    private String makeQueryWords(String paramName, String[] queryWordArr) {
        String resultQueryWords = "";

        if(queryWordArr.length == 1) {
            resultQueryWords = paramName + ":*" + queryWordArr[0] + "*";
        } else {
            int index = 0;

            for(String tmpQueryWord : queryWordArr) {
                if(index == 0) {
                    resultQueryWords = paramName + ":" + "\"*" + tmpQueryWord + "*\"";
                    index++;
                    continue;
                }

                if(index == queryWordArr.length - 1) {
                    if(!"".equals(tmpQueryWord.trim())) {
                        resultQueryWords = resultQueryWords + " AND " + paramName + ":" + tmpQueryWord;
                        index++;
                        continue;
                    }
                }

                resultQueryWords = resultQueryWords + " AND " + paramName + ":" + "\"" + tmpQueryWord + "\"";
                index++;
            }
        }

        return resultQueryWords;
    }

    private JSONObject getAutoCompleteResultList(AutoCompleteType autoCompleteType, String queryWords, String tenantId,
            List<AutoCompleteQueryParamVO> queryParamList, boolean isSearchAgain) throws Exception {
        JSONObject result = null;
        AutoCompleteTypeVO autoCompleteTypeVO = autoCompleteType.selectAutoCompleteData();
        Map<String, String> params = new HashMap<String, String>();
        String subParams = this.makeSubParams(queryParamList);

        if(tenantId != null) {
            params.put("q", queryWords + " AND tenantId:" + tenantId + subParams);
        } else {
            params.put("q", queryWords + subParams);
        }

        params.put("wt", "json");
        params.put("indent", "false");

        String resultStr = httpConnect.sendGet(autoCompleteUrl + autoCompleteTypeVO.getCore() + autoCompleteTypeVO.getActionPrefix(), null,
                params, charset);

        result = JSONObject.fromObject(resultStr);

        LOGGER.debug("call solr result : " + resultStr);

        int resultNum = result.getJSONObject("response").getInt("numFound");

        if(resultNum == 0) {
            if(isSearchAgain) {
                result = this.getAutoCompleteResultList(autoCompleteType, queryWords + "*", tenantId, queryParamList, false);
            }
        }

        return result;
    }

    private String makeSubParams(List<AutoCompleteQueryParamVO> queryParamList) {
        String subParams = "";
        int index = 0;

        for(AutoCompleteQueryParamVO tmpVo : queryParamList) {
            if(index == 0) {
                index++;
                continue;
            }

            subParams = subParams + " AND " + tmpVo.getParamName() + ":" + tmpVo.getParamValue();
        }

        return subParams;
    }

    @Override
    public void importCoreDataToAutoCompleteServer(String coreName) throws Exception {
        String dataImportURL = autoCompleteUrl + "/" + coreName + "/" + "dataimport";
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> params = new HashMap<String, String>();

        headers.put("Content-Type", "application/x-www-form-urlencoded");

        params.put("command", "delta-import");
        params.put("clean", "false");
        params.put("commit", "true");
        params.put("wt", "json");
        params.put("indent", "true");
        params.put("verbose", "false");
        params.put("optimize", "false");
        params.put("debug", "false");

        httpConnect.sendPost(dataImportURL, headers, params, charset);
    }
}
