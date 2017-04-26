package com.hs.gms.srv.api.common.treemodel;

import java.io.Serializable;

public class UserTreeStatusVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1154830748587607352L;

    private String procId;
    private int depth;

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
