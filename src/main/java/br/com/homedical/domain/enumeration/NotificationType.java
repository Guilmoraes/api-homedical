package br.com.homedical.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public enum NotificationType {

    MAIL,
    PUSH;

    @JsonCreator
    public static NotificationType findByValue(String value) {
        for (NotificationType p : values()) {
            if (p.getValue().equalsIgnoreCase(value)) {
                return p;
            }
        }
        if (StringUtils.isNumeric(value) && NumberUtils.isParsable(value)) {
            Integer ordinal = NumberUtils.toInt(value);
            if (ordinal < values().length) {
                return values()[ordinal];
            }
        }

        return null;
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
