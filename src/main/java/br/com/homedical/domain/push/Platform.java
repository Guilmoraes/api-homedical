package br.com.homedical.domain.push;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Platform {

    ANDROID("android"),
    IOS("ios"),
    WEB("web");

    private final String value;

    Platform(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Platform findByValue(String value) {
        for (Platform p : values()) {
            if (p.getValue().equalsIgnoreCase(value)) {
                return p;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
