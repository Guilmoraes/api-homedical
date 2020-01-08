package br.com.homedical.config.push;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FCMCredentials {

    @NotNull
    @Size(min = 1, max = 255, message = "Firebase Cloud Messaging Key must be max. 255 chars long")
    private String key;

    public FCMCredentials(String key) {
        this.key = key;
    }

    public String getApiKey() {
        return key;
    }

}
