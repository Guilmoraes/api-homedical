package br.com.homedical.event;

import org.springframework.context.ApplicationEvent;

public class DocumentChangeStatusEmailEvent extends ApplicationEvent {

    public DocumentChangeStatusEmailEvent(String userId) {
        super(userId);
    }
}
