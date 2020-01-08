package br.com.homedical.config.push;

import br.com.homedical.config.ApplicationProperties;
import br.com.homedical.domain.push.Platform;
import br.com.homedical.service.push.PushSenderService;
import br.com.homedical.service.push.SenderType;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class PushConfiguration {

    private final ApplicationContext appContext;

    public PushConfiguration(ApplicationProperties properties,
                             ApplicationContext appContext) {
        this.appContext = appContext;
    }

    /**
     * Valida os beans de envio de push, que devem ser para cada tipo de provider
     */
    @PostConstruct
    public void validatePushServices() {

        Set<Platform> platforms =
            appContext.getBeansOfType(PushSenderService.class)
                .entrySet()
                .stream()
                .map(entry ->
                    AopProxyUtils
                        .ultimateTargetClass(entry.getValue())
                        .getAnnotation(SenderType.class)
                        .values())
                .flatMap(Stream::of)
                .collect(Collectors.toSet());

        Arrays.stream(Platform.values()).forEach(p -> {
            if (!platforms.contains(p)) {
                throw new IllegalArgumentException("Platform Enum need to have the same number of respective PushSenderService beans. Did you create a new Platform type and don't create the PushSenderService implementation for this type?");
            }
        });
    }

}
