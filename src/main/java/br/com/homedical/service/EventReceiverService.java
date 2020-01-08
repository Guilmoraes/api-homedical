package br.com.homedical.service;

import br.com.homedical.event.*;
import br.com.homedical.service.jms.JmsDestinationQueue;
import br.com.homedical.service.jms.types.StringType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

public interface EventReceiverService {

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processRegistrationEmailEvent(RegistrationEmailEvent event);

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processPasswordRecoveryEmailEvent(PasswordRecoveryEmailEvent event);

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processProfessionalRegistrationEmailEvent(ProfessionalRegistrationEmailEvent event);

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processProfessionalRegistrationByAdminEmailEvent(ProfessionalRegistrationByAdminEmailEvent event);

    @Async
    @TransactionalEventListener(fallbackExecution = true)
    void processDocumentChangeStatusEmailEvent(DocumentChangeStatusEmailEvent event);

}
