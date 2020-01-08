package br.com.homedical.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

/**
 * Created by rene on 3/5/18.
 */
@Configuration
@EnableJms
public class JmsConfiguration {

    public static final String DEFAULT_FACTORY = "defaultFactory";
    public static final String SINGLE_THREAD_FACTORY = "singleThreadFactory";

    @Bean
    @Primary
    @Qualifier(DEFAULT_FACTORY)
    public JmsListenerContainerFactory<?> defaultFactory(ConnectionFactory connectionFactory,
                                                         DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConcurrency("5-10");
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    @Primary
    @Qualifier(SINGLE_THREAD_FACTORY)
    public JmsListenerContainerFactory<?> singleThreadFactory(ConnectionFactory connectionFactory,
                                                              DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConcurrency("1");
        factory.setTaskExecutor(new SyncTaskExecutor());
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
