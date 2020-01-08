package br.com.homedical.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.aws.autoconfigure.context.ContextCredentialsAutoConfiguration;
import org.springframework.cloud.aws.core.config.AmazonWebserviceClientFactoryBean;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.cloud.aws.mail.simplemail.SimpleEmailServiceJavaMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@AutoConfigureAfter(org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class)
@Import(ContextCredentialsAutoConfiguration.class)
public class MailConfiguration {

    @Autowired(required = false)
    private RegionProvider regionProvider;

    @Bean
    public AmazonWebserviceClientFactoryBean<AmazonSimpleEmailServiceClient> amazonSimpleEmailService(AWSCredentialsProvider credentialsProvider) {
        return new AmazonWebserviceClientFactoryBean<>(AmazonSimpleEmailServiceClient.class,
            credentialsProvider, this.regionProvider);
    }

    @Bean
    public JavaMailSender mailSender(AmazonSimpleEmailService ses) {
        return new SimpleEmailServiceJavaMailSender(ses);
    }

}
