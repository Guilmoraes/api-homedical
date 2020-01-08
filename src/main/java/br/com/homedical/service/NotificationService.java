package br.com.homedical.service;

import br.com.homedical.domain.Installation;
import br.com.homedical.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    Notification save(Notification notification);

    Notification update(Notification notification);

    Page<Notification> findAll(Pageable pageable);

    Notification findOne(String id);

    void delete(String id);

    void sendEmailAccountCreated(String email);

    void sendPasswordResetMail(String userId);

    void sendProfessionalRegistrationEmail(String value);

    void sendProfessionalRegistrationByAdminEmail(String value);

    void sendChangeStatusDocumentEmail(String value);
  
    void confirm(Installation installation);
  
}
