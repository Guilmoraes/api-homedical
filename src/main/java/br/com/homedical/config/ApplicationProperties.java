package br.com.homedical.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Data
public class ApplicationProperties {

    private Push push = new Push();

    private Aws aws = new Aws();

    private String mapKey;

    private Authenticate authenticate = new Authenticate();

    @Data
    public static class Push {
        private String key;
    }

    @Data
    public static class Aws {

        private String bucketImages;

        private String bucketDocuments;

        private Long urlExpiration;

    }

    @Data
    public static class Authenticate {

        private Long daysToConfirmEmail;

    }

}
