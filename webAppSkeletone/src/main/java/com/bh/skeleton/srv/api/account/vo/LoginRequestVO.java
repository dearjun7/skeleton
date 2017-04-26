package com.hs.gms.srv.api.account.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.hs.gms.std.common.service.cache.type.DeviceType;

/**
 * 로그인을 위한 데이터를 담는 LoginRequestVO
 * 
 * @author BH Jun
 */
public class LoginRequestVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2687758575064432878L;

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String accountPw;
    private String deviceType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountPw() {
        return accountPw;
    }

    public void setAccountPw(String accountPw) {
        this.accountPw = accountPw;
    }

    public DeviceType getDeviceType() {
        DeviceType result = null;

        if(this.deviceType != null) {
            result = DeviceType.valueOf(deviceType);
        }
        return result;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType.toUpperCase();
    }
}
