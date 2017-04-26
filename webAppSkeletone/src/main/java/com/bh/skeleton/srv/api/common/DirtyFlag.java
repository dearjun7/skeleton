package com.hs.gms.srv.api.common;

public enum DirtyFlag {
    INSERT("I"),
    UPDATE("U"),
    DELETE("D");

    private String value;

    DirtyFlag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
