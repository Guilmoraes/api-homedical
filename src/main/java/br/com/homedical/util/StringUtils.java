package br.com.homedical.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String like(String param) {

        if (StringUtils.isBlank(param)) {
            return "";
        }

        return "%" + param.toLowerCase() + "%";
    }

}
