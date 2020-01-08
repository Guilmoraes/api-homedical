package br.com.homedical.service.impl;

import br.com.homedical.event.*;
import br.com.homedical.service.EventReceiverService;
import br.com.homedical.service.jms.JmsDestinationQueue;
import br.com.homedical.service.jms.types.StringType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class EventReceiverServiceImpl implements EventReceiverService {

    private final JmsTemplate jmsTemplate;

    public EventReceiverServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    @Override
    public void processRegistrationEmailEvent(RegistrationEmailEvent event) {
        jmsTemplate.convertAndSend(JmsDestinationQueue.REGISTRATION_EMAIL_QUEUE, StringType.builder().value((String) event.getSource()).build());
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    @Override
    public void processPasswordRecoveryEmailEvent(PasswordRecoveryEmailEvent event) {
        jmsTemplate.convertAndSend(JmsDestinationQueue.FORGOT_PASSWORD_EMAIL_QUEUE, StringType.builder().value((String) event.getSource()).build());
    }

    @Override
    public void processProfessionalRegistrationEmailEvent(ProfessionalRegistrationEmailEvent event) {
        jmsTemplate.convertAndSend(JmsDestinationQueue.PROFESSIONAL_REGISTRATION_EMAIL_QUEUE, StringType.builder().value((String) event.getSource()).build());
    }

    @Override
    public void processProfessionalRegistrationByAdminEmailEvent(ProfessionalRegistrationByAdminEmailEvent event) {
        jmsTemplate.convertAndSend(JmsDestinationQueue.PROFESSIONAL_REGISTRATION_BY_ADMIN_EMAIL_QUEUE, StringType.builder().value((String) event.getSource()).build());
    }

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    @Override
    public void processDocumentChangeStatusEmailEvent(DocumentChangeStatusEmailEvent event) {
        jmsTemplate.convertAndSend(JmsDestinationQueue.CHANGE_STATUS_DOCUMENT_QUEUE, StringType.builder().value((String) event.getSource()).build());
    }
}
