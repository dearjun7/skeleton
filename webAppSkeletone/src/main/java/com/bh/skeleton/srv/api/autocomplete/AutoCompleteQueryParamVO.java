package com.hs.gms.srv.api.autocomplete;

import java.io.Serializable;

/**
 * AutoCompleteQueryParamVO
 * 
 * @author BH Jun
 */
public class AutoCompleteQueryParamVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3922686833000706649L;

    private String paramName;
    private String paramValue;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
