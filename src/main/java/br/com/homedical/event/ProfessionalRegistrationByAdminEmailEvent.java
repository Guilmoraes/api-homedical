package br.com.homedical.event;

import org.springframework.context.ApplicationEvent;

public class ProfessionalRegistrationByAdminEmailEvent extends ApplicationEvent {

    public ProfessionalRegistrationByAdminEmailEvent(String id) {
        super(id);
    }
}
