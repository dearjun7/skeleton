package com.hs.gms.srv.api.system.code;

import java.util.List;

/**
 * CodeService
 * 
 * @author BH Jun
 */
public interface CodeService {

    public List<CodeVO> getCodeList(int pageViewSize, int currentPageNum) throws Exception;

    public int getCodeTotalCount() throws Exception;
}
