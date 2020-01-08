package br.com.homedical.event;

import org.springframework.context.ApplicationEvent;

public class PasswordRecoveryEmailEvent extends ApplicationEvent {

    public PasswordRecoveryEmailEvent(String userId) {
        super(userId);
    }
}
