package com.hs.gms.srv.api.account.error;

import com.hs.gms.std.common.error.ErrorCode;

/**
 * Account 처리 관련 Error Code.
 * 
 * @author BH Jun
 */
public enum AccountErrorCode implements ErrorCode {
    ACCESS_TOKEN_IS_NULL("ACN0N01-401"),
    //    ALREADY_LOGGED_IN("ACN0N04-409"),

    ACCESS_DENIED("ACN1N00-403"),
    INVALID_TOKEN("ACN1N01-401"),
    TOKEN_IS_EXPIRED("ACN1N02-401"),
    DOES_NOT_MATCH_TOKEN_VERSION("ACN1N03-401"),
    LOGEDIN_ANOTHER_CLIENT("ACN1N04-401"),

    SECRET_KEY_IS_NULL("ACN2N00-500"),
    TOKEN_VERSION_IS_NULL("ACN2N01-500"),

    LOGIN_FAIL_LOCK("ACN3N00-401"),

    // Directory error
    DIRECTORY_SYSTEM_ERROR("DIR0N00-500"),
    DIRECTORY_LOGIN_ERROR("DIR0N01-401"),
    DIRECTORY_LINKAGE_ERROR("DIR1N00-401");

    private String errorCode;

    AccountErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
