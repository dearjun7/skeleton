package com.hs.gms.srv.api.system.globaloption;

import java.io.Serializable;

/**
 * GlobalOptionVO
 * 
 * @author BH Jun
 */
public class GlobalOptionVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1262494083815399641L;

    private String optionId;
    private String optionValue;
    private String optionName;
    private String optionDesc;
    private String category;
    private int dispOrder;

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionDesc() {
        return optionDesc;
    }

    public void setOptionDesc(String optionDesc) {
        this.optionDesc = optionDesc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(int dispOrder) {
        this.dispOrder = dispOrder;
    }
}
