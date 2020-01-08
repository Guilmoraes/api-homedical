package br.com.homedical.web.rest.util;

import br.com.homedical.security.AuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockAdminSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockUser annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.authorities())
            .map(it -> new SimpleGrantedAuthority(it.startsWith("ROLE_") ? it : ("ROLE_" + it)))
            .collect(Collectors.toList());

        String customer = null;

        if (StringUtils.isNotBlank(annotation.user())) {
            customer = annotation.user();
        }

        AuthenticationToken token = new AuthenticationToken(annotation.login(), annotation.user(), customer, authorities, null);

        context.setAuthentication(token);
        return context;
    }
}
