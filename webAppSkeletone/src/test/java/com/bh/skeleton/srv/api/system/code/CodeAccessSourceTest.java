package com.hs.gms.srv.api.system.code;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.std.common.service.cache.TenantCacheAccessor;
import com.hs.gms.std.common.service.i18n.SupportedLocaleLang;
import com.hs.gms.std.common.test.AbstractApplicationContext;

import net.sf.json.JSONObject;

public class CodeAccessSourceTest extends AbstractApplicationContext {

    private final String pCode = "GMS_PROCTMPLTGROUP";
    private final String code = "testCode";
    private final String codeDesc = "testCodeDesc";
    private final int dispOrder = 1;
    private final String installFlag = "Y";

    private final String korCodeName = "korean";
    private final String engCodeName = "english";
    private final String japCodeName = "japenese";
    private final String chnCodeName = "chinese";

    @Mock
    private TenantCacheAccessor tenantCacheAccessor;

    @InjectMocks
    private CodeAccessSource codeService = new CodeAccessSource();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Mockito.when(tenantCacheAccessor.getTenantCommonCodeList(super.testTenantId, pCode)).thenAnswer(new Answer<JSONObject>() {

            @Override
            public JSONObject answer(InvocationOnMock invocation) throws Throwable {
                Map<String, List<CodeVO>> resultMap = new HashMap<String, List<CodeVO>>();

                for(SupportedLocaleLang locale : SupportedLocaleLang.values()) {
                    List<CodeVO> codeList = new ArrayList<CodeVO>();
                    CodeVO codeVo = new CodeVO();

                    codeVo.setTenantId(testTenantId);
                    codeVo.setpCode(pCode);
                    codeVo.setCode(code);
                    codeVo.setCodeDesc(codeDesc);
                    codeVo.setDispOrder(dispOrder);
                    codeVo.setInstallFlag(installFlag);

                    if("ko".equals(locale.toString())) {
                        codeVo.setCodeName(korCodeName);
                    } else if("en".equals(locale.toString())) {
                        codeVo.setCodeName(engCodeName);
                    } else if("ja".equals(locale.toString())) {
                        codeVo.setCodeName(japCodeName);
                    } else if("zh".equals(locale.toString())) {
                        codeVo.setCodeName(chnCodeName);
                    }

                    codeList.add(codeVo);

                    resultMap.put(locale.toString(), codeList);
                }

                return JSONObject.fromObject(resultMap);
            }
        });
    }

    @Test
    public void testGetCodeList() throws Exception {
        UserDataVO userData = new UserDataVO();

        userData.setLanguage("ko");
        userData.setTenantId(super.testTenantId);
        UserDataContextHolder.setUserData(userData);

        List<CodeVO> result = codeService.getTenantCodeListFromCache(pCode);

        for(CodeVO tmp : result) {
            assertSame(pCode, tmp.getpCode());
            assertSame(code, tmp.getCode());
            assertSame(codeDesc, tmp.getCodeDesc());
            assertSame(dispOrder, tmp.getDispOrder());
            assertSame(installFlag, tmp.getInstallFlag());
            assertSame(super.testTenantId, tmp.getTenantId());
            assertSame(korCodeName, tmp.getCodeName());
        }
    }

}
