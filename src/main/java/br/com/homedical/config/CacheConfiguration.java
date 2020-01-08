package br.com.homedical.config;

import br.com.homedical.domain.File;
import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = {MetricsConfiguration.class})
@AutoConfigureBefore(value = {WebConfigurer.class, DatabaseConfiguration.class})
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(br.com.homedical.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Installation.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Photo.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Country.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.State.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.City.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.File.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Signature.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Address.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Phone.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Specialty.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Notification.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Document.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Professional.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Patient.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.HealthOperator.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.DocumentType.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Schedule.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Duty.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Schedule.class.getName() + ".signatures", jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Schedule.class.getName() + ".scheduleImages", jcacheConfiguration);
            cm.createCache(br.com.homedical.domain.Professional.class.getName() + ".duties", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
