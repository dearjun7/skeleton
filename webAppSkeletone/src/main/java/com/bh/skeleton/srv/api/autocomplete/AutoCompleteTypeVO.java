package com.hs.gms.srv.api.autocomplete;

import java.io.Serializable;

/**
 * AutoCompleteTypeVO
 * 
 * @author BH Jun
 */
public class AutoCompleteTypeVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7530625336696731506L;

    private String core;
    private String actionPrefix;
    private String[] subParamNameArr; // 검색 할 검색어 키워드와 조함되어 검색할 서브 검색 조건(ex > 상위코드 등등...)

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getActionPrefix() {
        return actionPrefix;
    }

    public void setActionPrefix(String actionPrefix) {
        this.actionPrefix = actionPrefix;
    }

    public String[] getSubParamNameArr() {
        return subParamNameArr;
    }

    public void setSubParamNameArr(String[] subParamNameArr) {
        this.subParamNameArr = subParamNameArr;
    }
}
