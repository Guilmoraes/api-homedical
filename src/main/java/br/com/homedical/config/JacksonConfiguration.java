package br.com.homedical.config;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
public class JacksonConfiguration {

    /**
     * Support for Hibernate types in Jackson.
     */
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    /**
     * Jackson Afterburner module to speed up serialization/deserialization.
     */
    @Bean
    public AfterburnerModule afterburnerModule() {
        return new AfterburnerModule();
    }

    @Autowired
    private Jackson2ObjectMapperBuilder builder;

    @Bean
    public MappingJackson2HttpMessageConverter getMessageConverter(ObjectMapper mapper) {
        return new MappingJackson2HttpMessageConverter(mapper);
    }

    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper() {
        return this.builder
            .createXmlMapper(false)
            .build()
            .registerModule(new JtsModule())
            .registerModule(new JavaTimeModule())
            .findAndRegisterModules()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setDefaultPrettyPrinter(new MinimalPrettyPrinter());
    }

    @Bean
    @Qualifier("PUSH")
    public ObjectMapper pushJacksonObjectMapper() {
        return this.builder
            .createXmlMapper(false)
            .build()
            .registerModule(new JtsModule())
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setDefaultPrettyPrinter(new MinimalPrettyPrinter());
    }
}
