package br.com.homedical.service.jms;

import br.com.homedical.config.JmsConfiguration;
import br.com.homedical.service.NotificationService;
import br.com.homedical.service.jms.types.StringType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class JmsReceiver {

    private final NotificationService notificationService;

    public JmsReceiver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @JmsListener(destination = JmsDestinationQueue.REGISTRATION_EMAIL_QUEUE, containerFactory = JmsConfiguration.DEFAULT_FACTORY)
    public void receiveRegistrationEmailQueue(StringType type) {
        notificationService.sendEmailAccountCreated(type.getValue());
    }

    @JmsListener(destination = JmsDestinationQueue.FORGOT_PASSWORD_EMAIL_QUEUE, containerFactory = JmsConfiguration.DEFAULT_FACTORY)
    public void receivePasswordRecoveryEmailQueue(StringType type) {
        notificationService.sendPasswordResetMail(type.getValue());
    }

    @JmsListener(destination = JmsDestinationQueue.PROFESSIONAL_REGISTRATION_EMAIL_QUEUE, containerFactory = JmsConfiguration.DEFAULT_FACTORY)
    public void receiveProfessionalRegistrationQueue(StringType type) {
        notificationService.sendProfessionalRegistrationEmail(type.getValue());
    }

    @JmsListener(destination = JmsDestinationQueue.PROFESSIONAL_REGISTRATION_BY_ADMIN_EMAIL_QUEUE, containerFactory = JmsConfiguration.DEFAULT_FACTORY)
    public void receiveProfessionalRegistrationByAdminEmailQueue(StringType type) {
        notificationService.sendProfessionalRegistrationByAdminEmail(type.getValue());
    }

    @JmsListener(destination = JmsDestinationQueue.CHANGE_STATUS_DOCUMENT_QUEUE, containerFactory = JmsConfiguration.DEFAULT_FACTORY)
    public void receiveChangeStatusDocumentEmail(StringType type) {
        notificationService.sendChangeStatusDocumentEmail(type.getValue());
    }

}
