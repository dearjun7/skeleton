package com.hs.gms.srv.api.common.workflow.type;

public enum WorkProcessType {
    CHANGE("C"),
    ISSUE("I"),
    ISOINTRO("S"),
    ISOINTRO_PROC("P");

    private String workProcessType;

    private WorkProcessType(String workProcessType) {
        this.workProcessType = workProcessType;
    }

    public String getValue() {
        return this.workProcessType;
    }

    public static WorkProcessType getWorkProcessType(String workProcessType) {
        WorkProcessType result = null;

        for(WorkProcessType stateValue : WorkProcessType.values()) {
            if(stateValue.getValue().equals(workProcessType)) {
                result = stateValue;
            }
        }

        return result;
    }
}
