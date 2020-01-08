package br.com.homedical.web.rest.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAdminSecurityContextFactory.class)
public @interface WithMockUser {

    String login() default "admin";

    String user() default "3";

    String[] authorities() default {"ADMIN"};


}
