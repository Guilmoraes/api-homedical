package br.com.homedical.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public enum HealthOperatorModality {

    SPECIALIZED_HEALTH_INSURANCE_COMPANY,
    MEDICINE_GROUP,
    MEDICAL_COOPERATIVE,
    DENTAL_COOPERATIVE,
    DENTISTRY_GROUP,
    PHILANTHROPY,
    SELF_MANAGEMENT,
    MULTINATIONAL;

    @JsonCreator
    public static HealthOperatorModality findByValue(String value) {
        for (HealthOperatorModality p : values()) {
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
