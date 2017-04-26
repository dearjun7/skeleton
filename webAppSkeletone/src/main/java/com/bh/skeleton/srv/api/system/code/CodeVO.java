package com.hs.gms.srv.api.system.code;

import java.io.Serializable;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * CodeVO
 * 
 * @author BH Jun
 */
public class CodeVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1248207981611783151L;

    private String tenantId;
    @NotEmpty
    @Length(min = 1, max = 30)
    private String code;
    @NotEmpty
    @Length(min = 1, max = 30)
    private String pCode;
    @Length(min = 3, max = 100)
    private String codeName;
    @Length(min = 0, max = 2000)
    private String codeDesc;
    @Pattern(regexp = "^[Y|N]{1}$")
    private String installFlag;
    @DecimalMax(value = "100")
    private int dispOrder;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public String getInstallFlag() {
        return installFlag;
    }

    public void setInstallFlag(String installFlag) {
        this.installFlag = installFlag;
    }

    public int getDispOrder() {
        return dispOrder;
    }

    public void setDispOrder(int dispOrder) {
        this.dispOrder = dispOrder;
    }
}
