package com.hs.gms.srv.api.system.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.service.cache.TenantCacheAccessor;
import com.hs.gms.std.common.service.i18n.SupportedLocaleLang;
import com.hs.gms.std.common.util.JSONConverter;

import net.sf.json.JSONObject;

/**
 * CodeAccessSource
 * 
 * @author BH Jun
 */
@Component("CodeAccessSource")
public class CodeAccessSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeAccessSource.class);

    @Value("#{config['gms.common.system.tenantid']}")
    private String systemTenantId;

    @Autowired
    private TenantCacheAccessor tenantCacheAccessor;
    @Autowired
    private CodeDAO codeDao;

    private boolean reload = false;

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public void init() throws Exception {
        if(reload) {
            LOGGER.info("Init Loading CommonCode");

            this.flushAllCodeDataFromCache();
            this.loadAllCodeDataToCache();
        }
    }

    public List<CodeVO> getTenantCodeListFromCache(String pCode) throws Exception {
        UserDataVO userData = UserDataContextHolder.getUserData();
        JSONObject resultJson = tenantCacheAccessor.getTenantCommonCodeList(userData.getTenantId(), pCode);

        List<CodeVO> result = JSONConverter.jsonObjectToList(resultJson, userData.getLanguage(), CodeVO.class);

        return result;
    }

    public CodeVO getTenantCodeFromCache(String pCode, String code) throws Exception {
        UserDataVO userData = UserDataContextHolder.getUserData();
        JSONObject resultJson = tenantCacheAccessor.getTenantCommonCodeList(userData.getTenantId(), pCode);
        List<CodeVO> codeList = JSONConverter.jsonObjectToList(resultJson, userData.getLanguage(), CodeVO.class);
        CodeVO result = null;

        for(CodeVO tmpCodeVO : codeList) {
            if(tmpCodeVO.getCode().equals(code)) {
                result = tmpCodeVO;
                break;
            }
        }

        return result;
    }

    public List<CodeVO> getSystemCodeListFromCache(String pCode) throws Exception {
        JSONObject resultJson = tenantCacheAccessor.getTenantCommonCodeList(this.systemTenantId, pCode);
        String language = UserDataContextHolder.getUserData().getLanguage();

        List<CodeVO> result = JSONConverter.jsonObjectToList(resultJson, language, CodeVO.class);

        return result;
    }

    public CodeVO getSystemCodeFromCache(String pCode, String code) throws Exception {
        JSONObject resultJson = tenantCacheAccessor.getTenantCommonCodeList(this.systemTenantId, pCode);
        String language = UserDataContextHolder.getUserData().getLanguage();
        List<CodeVO> codeList = JSONConverter.jsonObjectToList(resultJson, language, CodeVO.class);
        CodeVO result = null;

        for(CodeVO tmpCodeVO : codeList) {
            if(tmpCodeVO.getCode().equals(code)) {
                result = tmpCodeVO;
                break;
            }
        }

        return result;
    }

    //    public void loadCodeDataToCache(String pCode) throws Exception {
    //        String tenantId = UserDataContextHolder.getUserData().getTenantId();
    //        Map<String, List<CodeVO>> codeMap = this.getCodeListMapInAllLocaleByParentCode(tenantId, pCode);
    //
    //        tenantCacheAccessor.setTenantCommonCodeList(tenantId, pCode, codeMap);
    //    }
    //
    //    public void flushCodeDataToCache(String pCode) throws Exception {
    //        String tenantId = UserDataContextHolder.getUserData().getTenantId();
    //
    //        tenantCacheAccessor.removeTenantCommonCodeList(tenantId, pCode);
    //    }

    public void loadCodeDataToCacheByTenantId(String tenantId) throws Exception {
        List<String> pCodeAllList = codeDao.selectAllPcodeListByTenantId(tenantId);

        this.setCodeToCache(pCodeAllList, tenantId);
    }

    public void flushCodeDataToCacheByTenantId(String tenantId) throws Exception {
        List<String> pCodeAllList = codeDao.selectAllPcodeListByTenantId(tenantId);

        this.removeCodeToCache(pCodeAllList, tenantId);
    }

    private void loadAllCodeDataToCache() throws Exception {
        List<String> tenantIdAllListFromCode = codeDao.selectSomeColumnListInCodeData(GatheringEnableColumnType.TENANT_ID);

        for(String tenantId : tenantIdAllListFromCode) {
            List<String> pCodeAllList = codeDao.selectSomeColumnListInCodeData(GatheringEnableColumnType.P_CODE);

            Map<String, List<CodeVO>> pCodeMap = this.getCodeListMapInAllLocaleByParentCode(tenantId, "0");
            tenantCacheAccessor.setTenantCommonCodeList(tenantId, "0", pCodeMap);

            this.setCodeToCache(pCodeAllList, tenantId);
        }
    }

    private void flushAllCodeDataFromCache() throws Exception {
        List<String> tenantIdAllListFromCode = codeDao.selectSomeColumnListInCodeData(GatheringEnableColumnType.TENANT_ID);

        for(String tenantId : tenantIdAllListFromCode) {
            List<String> pCodeAllList = codeDao.selectSomeColumnListInCodeData(GatheringEnableColumnType.P_CODE);

            this.removeCodeToCache(pCodeAllList, tenantId);
        }
    }

    private void setCodeToCache(List<String> pCodeAllList, String tenantId) throws Exception {
        for(String pCode : pCodeAllList) {
            Map<String, List<CodeVO>> codeMap = this.getCodeListMapInAllLocaleByParentCode(tenantId, pCode);

            tenantCacheAccessor.setTenantCommonCodeList(tenantId, pCode, codeMap);
        }
    }

    private void removeCodeToCache(List<String> pCodeAllList, String tenantId) throws Exception {
        for(String pCode : pCodeAllList) {
            tenantCacheAccessor.removeTenantCommonCodeList(tenantId, pCode);
        }
    }

    private Map<String, List<CodeVO>> getCodeListMapInAllLocaleByParentCode(String tenantId, String pCode) {
        Map<String, List<CodeVO>> result = new HashMap<String, List<CodeVO>>();

        for(SupportedLocaleLang tmpLang : SupportedLocaleLang.values()) {
            List<CodeVO> codeList = codeDao.selectCodeListForCaching(tenantId, pCode, tmpLang.toString());

            if(codeList.size() > 0) {
                result.put(tmpLang.toString(), codeList);
            }
        }

        return result;
    }
}
