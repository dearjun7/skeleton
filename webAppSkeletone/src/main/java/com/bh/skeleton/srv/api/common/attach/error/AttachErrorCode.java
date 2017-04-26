package com.hs.gms.srv.api.common.attach.error;

import com.hs.gms.std.common.error.ErrorCode;

/**
 * AttachErrorCode
 * 
 * @author BH Jun
 */
public enum AttachErrorCode implements ErrorCode {
    ATTACH_FAIL("ATC0N01-400"),
    ATTACH_BAD_REQ("ATC0N02-400"),
    ATTACH_NOT_FOUND_FILE("ATC0N03-404");

    private String errorCode;

    AttachErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
