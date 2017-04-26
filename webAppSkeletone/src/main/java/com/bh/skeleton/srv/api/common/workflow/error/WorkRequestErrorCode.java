package com.hs.gms.srv.api.common.workflow.error;

import com.hs.gms.std.common.error.ErrorCode;

public enum WorkRequestErrorCode implements ErrorCode {
    UNAUTHORIZED("WFL0N00-401"),
    DOSE_NOT_EXIST_REQUEST_STATE("WFL0N01-412"),
    INCORRECT_WORK_ACTOIN_TYPE("WFL0N02-412"),
    ALREADY_FINISH_REQUEST("WFL0N03-400"),
    HAS_NO_PROCESSING_STATE("WFL0N04-400"),
    ALREADY_APPROVE_REQUEST("WFL0N05-400"),
    OTHER_USER_PROCESSING("WFL0N06-406"),
    HAS_NO_CHANGE_REQUEST("WFL0N07-404"),
    NOT_ALLOW_PUBLISHDATE_THEN_TODAY("WFL0N08-400"),
    ILLEGAL_WORKSTATE_REQUEST("WFL0N09-404");

    private String errorCode;

    WorkRequestErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
