package com.hs.gms.srv.api.account.vo;

import java.io.Serializable;

/**
 * UserClaim
 * 
 * @author BH Jun
 */
public class UserClaim implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3478800461455493302L;

    private String issuer;
    private String tenantId;
    private String userId;
    private String deviceType;
    private String audience;
    private long issueAt;
    private long expireTime;
    private String version;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public long getIssueAt() {
        return issueAt;
    }

    public void setIssueAt(long issueAt) {
        this.issueAt = issueAt;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
