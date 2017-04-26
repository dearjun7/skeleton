package com.hs.gms.srv.api.common.workflow.vo;

import java.io.Serializable;

/**
 * WorkItem
 * 
 * @author Ma, JoonChae
 */
public class PrtcpVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4979242966236761044L;

    private String prtcp;
    private String prtcpAuth;
    private String prtcpType;
    private String prtcpName;

    public String getPrtcp() {
        return prtcp;
    }

    public void setPrtcp(String prtcp) {
        this.prtcp = prtcp;
    }

    public String getPrtcpAuth() {
        return prtcpAuth;
    }

    public void setPrtcpAuth(String prtcpAuth) {
        this.prtcpAuth = prtcpAuth;
    }

    public String getPrtcpType() {
        return prtcpType;
    }

    public void setPrtcpType(String prtcpType) {
        this.prtcpType = prtcpType;
    }

    public String getPrtcpName() {
        return prtcpName;
    }

    public void setPrtcpName(String prtcpName) {
        this.prtcpName = prtcpName;
    }

}
