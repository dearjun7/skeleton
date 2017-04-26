package com.hs.gms.srv.api.system.license;

/**
 * LicenseType enum
 * 
 * @author JS Park
 */
public enum LicenseType {
    PROCESS_MANAGER("PAM"),
    SYSTEM_MANAGER("SAM"),
    USER("USR");

    private String value;

    LicenseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
