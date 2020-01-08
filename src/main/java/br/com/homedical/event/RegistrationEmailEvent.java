package br.com.homedical.event;

import org.springframework.context.ApplicationEvent;

public class RegistrationEmailEvent extends ApplicationEvent {

    public RegistrationEmailEvent(String userId) {
        super(userId);
    }
}
