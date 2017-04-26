package com.hs.gms.srv.api.system.code;

/**
 * GatheringEnableColumnType
 * 
 * @author BH Jun
 */
public enum GatheringEnableColumnType {
    TENANT_ID("tenantId"),
    P_CODE("pCode");

    private String columnName;

    GatheringEnableColumnType(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }
}
