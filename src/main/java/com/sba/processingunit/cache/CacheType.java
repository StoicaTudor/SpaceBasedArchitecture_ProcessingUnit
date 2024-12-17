package com.sba.processingunit.cache;

public enum CacheType {
    USER("User"),
    UPDATES("Updates");

    private final String value;

    CacheType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
