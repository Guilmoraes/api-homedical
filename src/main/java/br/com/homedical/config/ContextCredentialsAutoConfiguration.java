package br.com.homedical.config;

import com.amazonaws.auth.profile.internal.AwsProfileNameLoader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.aws.context.config.annotation.ContextDefaultConfigurationRegistrar;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import static br.com.homedical.config.ContextConfigurationUtils.registerDefaultAWSCredentialsProvider;
import static org.springframework.cloud.aws.context.config.support.ContextConfigurationUtils.registerCredentialsProvider;
import static org.springframework.cloud.aws.context.config.support.ContextConfigurationUtils.registerRegionProvider;

@Configuration
@Import({ContextDefaultConfigurationRegistrar.class, ContextCredentialsAutoConfiguration.Registrar.class})
@ConditionalOnClass(name = "com.amazonaws.auth.AWSCredentialsProvider")
public class ContextCredentialsAutoConfiguration {

    public static class Registrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

        private Environment environment;

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            Boolean useDefaultCredentialsChain = this.environment.getProperty("cloud.aws.credentials.useDefaultAwsCredentialsChain", Boolean.class, false);

            registerRegionProvider(registry,
                this.environment.getProperty("cloud.region.auto", Boolean.class, true),
                this.environment.getProperty("cloud.region.static"));

            if (useDefaultCredentialsChain) {
                registerDefaultAWSCredentialsProvider(registry);
            } else {
                registerCredentialsProvider(registry, this.environment.getProperty("cloud.aws.credentials.accessKey"),
                    this.environment.getProperty("cloud.aws.credentials.secretKey"),
                    this.environment.getProperty("cloud.aws.credentials.instanceProfile", Boolean.class, true) &&
                        !this.environment.containsProperty("cloud.aws.credentials.accessKey"),
                    this.environment.getProperty("cloud.aws.credentials.profileName", AwsProfileNameLoader.DEFAULT_PROFILE_NAME),
                    this.environment.getProperty("cloud.aws.credentials.profilePath"));
            }
        }
    }

}
