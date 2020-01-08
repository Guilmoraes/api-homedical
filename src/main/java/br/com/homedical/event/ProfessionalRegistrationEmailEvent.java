package br.com.homedical.event;

import org.springframework.context.ApplicationEvent;

public class ProfessionalRegistrationEmailEvent extends ApplicationEvent {

    public ProfessionalRegistrationEmailEvent(String userId) {
        super(userId);
    }
}
