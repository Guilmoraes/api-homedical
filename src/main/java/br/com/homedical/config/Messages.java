package br.com.homedical.config;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class Messages {

    private final MessageSource messageSource;

    private MessageSourceAccessor accessor;

    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
    }

    public String get(String code) {
        return accessor.getMessage(code);
    }

    public String get(String code, Locale locale) {
        return accessor.getMessage(code, locale);
    }

    public String get(String code, Locale locale, String... args) {
        return accessor.getMessage(code, args, locale);
    }

    public String get(String code, String... args) {
        return accessor.getMessage(code, args);
    }

    public String get(String code, String defaultMessage, String... args) {
        return accessor.getMessage(code, args, defaultMessage);
    }

    public String get(String code, String defaultMessage, Locale locale, String... args) {
        return accessor.getMessage(code, args, defaultMessage, locale);
    }

}
