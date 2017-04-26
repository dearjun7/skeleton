package com.hs.gms.srv.api.common.filestorage.error;

import com.hs.gms.std.common.error.ErrorCode;

/**
 * AttachErrorCode
 * 
 * @author BH Jun
 */
public enum FileStorageErrorCode implements ErrorCode {
    FILESTORAGE_SYSTEM_ERROR("FST0N00-500"),
    FILESTORAGE_UPLOAD_FAIL("FST0N01-400");

    private String errorCode;

    FileStorageErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
