package com.hs.gms.srv.api.account.vo;

import java.io.Serializable;

/**
 * AccessToken
 * 
 * @author BH Jun
 */
public class AccessToken implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 570866568001463265L;

    private String token;
    private String claim;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    @Override
    public String toString() {
        return "GMSAccessToken - claim : " + claim + ", token : " + token;
    }
}
